package sitschedule;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class ClassItem implements Comparable<ClassItem> {

    private String moduleName, component, location;
    private LocalDate date;
    private LocalTime startTime, endTime;

    ClassItem(String moduleName) {
        this.moduleName = moduleName;
    }

    void setDate(String date) {
        this.date = LocalDate.parse(date.substring(0, 10), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    void setTime(String time) {
        System.out.println(time);
        if (time.equals("TBA"))
            return;
        Matcher matcher = Pattern.compile("[A-Z][a-z] (\\d{1,2}:\\d{2}(?:AM|PM)) - (\\d{1,2}:\\d{2}(?:AM|PM))").matcher(time);
        matcher.matches();
//        System.out.println(matcher.group(1));
//        System.out.println(matcher.group(2));
        startTime = LocalTime.parse(matcher.group(1), DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH));
        endTime = LocalTime.parse(matcher.group(2), DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH));
    }

    String getTime() {
        return startTime == null ? null : startTime + " - " + endTime;
    }

    String getModuleName() {
        String moduleCode = moduleName.split(" - ")[0];
        String formattedName = Resource.modules.get(moduleCode);
        if (formattedName == null)
            return moduleName.replace(" - ", " ");
        return formattedName;
    }

    @Override
    public int compareTo(ClassItem o) {
        if (startTime == null)
            return -1;
        if (o.startTime == null)
            return 1;
        return LocalDateTime.of(date, startTime).compareTo(LocalDateTime.of(o.getDate(), o.getStartTime()));
    }
}
