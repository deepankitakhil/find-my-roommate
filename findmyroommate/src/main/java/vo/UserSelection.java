package vo;

/**
 * Created by akhil on 1/8/2017.
 */

public class UserSelection {
    private String sex;
    private String profession;
    private String dietaryPreference;
    private String searchCriteria;
    private String address;
    private String additionalPreference;

    public UserSelection(String sex, String profession, String dietaryPreference, String searchCriteria, String address, String additionalPreference) {
        this.sex = sex;
        this.profession = profession;
        this.dietaryPreference = dietaryPreference;
        this.searchCriteria = searchCriteria;
        this.address = address;
        this.additionalPreference = additionalPreference;
    }

    public UserSelection() {
    }

    public String getSex() {
        return sex;
    }

    public String getProfession() {
        return profession;
    }

    public String getDietaryPreference() {
        return dietaryPreference;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public String getAddress() {
        return address;
    }

    public String getAdditionalPreference() {
        return additionalPreference;
    }
}
