package vo;

/**
 * Created by akhil on 1/8/2017.
 */

public class UserSelection {
    private String desiredSex;
    private String desiredProfession;
    private String desiredDietaryPreference;
    private String searchCriteria;
    private String desiredLocationPreference;
    private String additionalPreference;

    public UserSelection(String desiredSex, String desiredProfession, String desiredDietaryPreference, String searchCriteria, String desiredLocationPreference, String additionalPreference) {
        this.desiredSex = desiredSex;
        this.desiredProfession = desiredProfession;
        this.desiredDietaryPreference = desiredDietaryPreference;
        this.searchCriteria = searchCriteria;
        this.desiredLocationPreference = desiredLocationPreference;
        this.additionalPreference = additionalPreference;
    }

    public String getDesiredSex() {
        return desiredSex;
    }

    public String getDesiredProfession() {
        return desiredProfession;
    }

    public String getDesiredDietaryPreference() {
        return desiredDietaryPreference;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public String getDesiredLocationPreference() {
        return desiredLocationPreference;
    }

    public String getAdditionalPreference() {
        return additionalPreference;
    }
}
