package vo;

/**
 * Created by akhil on 1/9/2017.
 */

public class SearchResult {
    private static final String BIO_IS_NOT_SET = "'s bio is not set.";
    private String name;
    private String profession;
    private String userBio;
    private String sex;
    private String dietaryPreference;
    private String additionalPreferences;
    private String address;

    public SearchResult(String name, String profession, String userBio, String sex, String dietaryPreference, String address, String additionalPreferences) {
        this.name = name;
        this.profession = profession;
        this.userBio = userBio;
        this.sex = sex;
        this.dietaryPreference = dietaryPreference;
        this.address = address;
        this.additionalPreferences = additionalPreferences;
    }

    @Override
    public String toString() {
        userBio = userBio != null ? userBio : name + BIO_IS_NOT_SET;
        return "Name: " + name + System.lineSeparator() +
                "Profession: " + profession + System.lineSeparator() +
                "UserBio: " + userBio + System.lineSeparator() +
                "Sex: " + sex + System.lineSeparator() +
                "DietaryPreference: " + dietaryPreference + System.lineSeparator() +
                "AdditionalPreferences: '" + additionalPreferences + System.lineSeparator() +
                "Address: " + address + System.lineSeparator();
    }

    public String getName() {
        return name;
    }

    public String getProfession() {
        return profession;
    }

    public String getUserBio() {
        return userBio;
    }

    public String getSex() {
        return sex;
    }

    public String getDietaryPreference() {
        return dietaryPreference;
    }

    public String getAddress() {
        return address;
    }

    public String getAdditionalPreferences() {
        return additionalPreferences;
    }
}
