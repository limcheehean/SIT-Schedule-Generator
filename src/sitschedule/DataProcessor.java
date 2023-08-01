package sitschedule;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.jsoup.Connection.Method.GET;
import static org.jsoup.Connection.Method.POST;

public class DataProcessor {

    private final String username, password;
    private final LocalDate trimesterStart, trimesterEnd;
    private final AppArea area;

    private void loadData() {

        try {
            // Load cookies
            area.append("Connecting to IN4SIT");
            String url = "https://fs.singaporetech.edu.sg/adfs/ls/idpinitiatedsignon.asmx?loginToRp=https://in4sit.singaporetech.edu.sg/psp/CSSISSTD/";
            System.out.println("Connecting");
            Response response = Jsoup.connect(url).method(GET).execute();
            System.out.println("Connected");
            Map<String, String> cookies = response.cookies();

            // Login
            area.append("Logging into IN4SIT");
            Map<String, String> data = Map.of(
                    "UserName", username,
                    "Password", password,
                    "AuthMethod", "FormsAuthentication"
            );
            url = response.parse().getElementById("options").attr("action");
            response = Jsoup.connect(url).method(POST).cookies(cookies).data(data).followRedirects(false).execute();
            // Status 200 if wrong credentials
            if (response.statusCode() == 200) {
                area.append("Error: Username or password is incorrect");
                return;
            }
            cookies.putAll(response.cookies());

            // Follow login redirect
            url = response.header("Location");
            assert url != null;
            response = Jsoup.connect(url).method(GET).cookies(cookies).execute();
            cookies.putAll(response.cookies());

            // Send SAML response
            area.append("Authenticating with SAML");
            data = Map.of("SAMLResponse", response.parse().getElementsByAttributeValue("name", "SAMLResponse").attr("value"));
            url = "https://in4sit.singaporetech.edu.sg/psc/CSSISSTD/EMPLOYEE/SA/c/NUI_FRAMEWORK.PT_LANDINGPAGE.GBL";
            response = Jsoup.connect(url).method(POST).cookies(cookies).data(data).execute();
            cookies.putAll(response.cookies());

            // Get schedule
            area.append("Getting schedule");
            url = "https://in4sit.singaporetech.edu.sg/psc/CSSISSTD/EMPLOYEE/SA/c/SA_LEARNER_SERVICES.SSR_SSENRL_LIST.GBL";
            response = Jsoup.connect(url).method(GET).cookies(cookies).execute();

            // Intermediate page
            Document document = response.parse();
            Element termTitle = document.selectFirst(".PSLEVEL1GRIDLABEL");
            if (termTitle != null && termTitle.text().equals("Select a term then select Continue.")) {
                Map<String, String> map = new HashMap<>();
                for (Element element: document.select("#win0divPSHIDDENFIELDS input"))
                    map.put(element.attr("name"), element.attr("value"));
                for (Element element: document.select("#ptus_universalSrch input"))
                    map.put(element.attr("name"), element.attr("value"));
                Element radio = document.selectFirst("input[title='Select this row']");
                map.put(radio.attr("name"), radio.attr("value"));
                map.put("ICAction", "DERIVED_SSS_SCT_SSR_PB_GO");
                response = Jsoup.connect(url).data(map).method(POST).cookies(cookies).execute();
                parseData(response.parse());
            } else {
                parseData(document);
            }

            // Parse schedule

        } catch (IOException e) {
            area.append("Error: " + e + "\n");
        }
    }

    private void parseData(Document document) {

        //System.out.println(document);
        area.append("Parsing schedule");
        List<ClassItem> classItems = new ArrayList<>();
        //System.out.println(document.text());
        String name = document.getElementsByAttributeValue("id", "DERIVED_SSTSNAV_PERSON_NAME").text();
        String period = document.getElementsByAttributeValueStarting("id", "DERIVED_REGFRM1_SSR_STDNTKEY_DESCR").text();
        String academicYear = period.substring(0, 7);
        String trimester = period.substring(18, 19);
        for (Element module: document.getElementsByAttributeValueStarting("id", "win0divDERIVED_REGFRM1_DESCR")) {
            Element moduleStatus = module.selectFirst("[id^=STATUS]");
            if (moduleStatus == null || !moduleStatus.text().equals("Enrolled"))
                continue;
            String moduleName = module.select(".PAGROUPDIVIDER").text();
            Elements components = module.getElementsByAttributeValueStarting("id", "MTG_COMP");
            Elements schedules = module.getElementsByAttributeValueStarting("id", "MTG_SCHED");
            Elements locations = module.getElementsByAttributeValueStarting("id", "MTG_LOC");
            Elements dates = module.getElementsByAttributeValueStarting("id", "MTG_DATES");
            String component = null;
            for (int i = 0; i < components.size(); i++) {
                ClassItem item = new ClassItem(moduleName);
                String currentComponent = components.get(i).text();
                if (!currentComponent.isEmpty())
                    component = currentComponent;
                item.setComponent(component);
                item.setDate(dates.get(i).text());
                item.setTime(schedules.get(i).text());
                item.setLocation(locations.get(i).text());
                classItems.add(item);
            }
        }
        Collections.sort(classItems);
        classItems.forEach(System.out::println);
        exportData(classItems, academicYear, trimester, name);
    }

    private void exportData(List<ClassItem> classItems, String academicYear, String trimester, String name) {

        area.append("Exporting schedule");
        Excel excel = new Excel();
        excel.nextRow();
        excel.setCells("SIT Schedule for Academic Year " + academicYear + " Trimester " + trimester + " for " + name);
        excel.setCellStyles(0, 6, "bold");
        excel.mergeCells(0, 6);
        excel.nextRow();
        excel.setCells("Day", "Date", "Time", "Module", "Type", "Location");
        excel.setCellStyles("bold");
        LocalDate date = trimesterStart;
        int week = 1;
        while (date.isBefore(trimesterEnd)) {
            excel.nextRow();
            excel.setCells("Week " + week++);
            excel.setCellStyles(0, 6, "center");
            excel.mergeCells(0, 6);
            LocalDate endWeek = date.plusDays(7);
            LocalDate finalDate = date;
            classItems.stream().filter(item -> (item.getDate().isEqual(finalDate) || item.getDate().isAfter(finalDate)) && item.getDate().isBefore(endWeek)).forEach(item -> {
                excel.nextRow();
                excel.setCells(item.getDate(), item.getDate(), item.getTime(), item.getModuleName(), item.getComponent(), item.getLocation());
                excel.setCellStyles(0, "day");
                excel.setCellStyles(1, "date");
                excel.setCellStyles(2, 4, "border");
            });
            date = endWeek;
        }

        excel.autoFit();
        excel.save("Schedule " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".xlsx");
        area.append("Schedule exported successfully");

        saveCredentials();
    }

    private void saveCredentials() {
        area.append("Saving credentials");
        try (FileWriter writer = new FileWriter("credentials")) {
            writer.write(Base64.getEncoder().encodeToString(("username=" + username + "\npassword=" + password).getBytes()));
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
        area.append("Complete!");
    }


    DataProcessor(String username, String password, String start, String end, AppArea area) {
        this.username = username;
        this.password = password;
        this.trimesterStart = LocalDate.parse(start, DateTimeFormatter.ofPattern("ddMMyyyy"));
        this.trimesterEnd = LocalDate.parse(end, DateTimeFormatter.ofPattern("ddMMyyyy"));
        this.area = area;
        loadData();
    }

}
