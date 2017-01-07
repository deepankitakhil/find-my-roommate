package vo;

/**
 * Created by akhil on 1/2/2017.
 */

public class User {
    private String name;
    private String phoneNumber;
    private String profession;
    private String userBio;
    private String email;
    private String sex;
    private String address;
    private String additionalPreferences;

    public User(String name, String phoneNumber, String profession, String userBio, String email,
                String sex, String address, String additionalPreferences) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profession = profession;
        this.userBio = userBio;
        this.email = email;
        this.sex = sex;
        this.address = address;
        this.additionalPreferences = additionalPreferences;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfession() {
        return profession;
    }

    public String getUserBio() {
        return userBio;
    }

    public String getEmail() {
        return email;
    }

    public String getSex() {
        return sex;
    }

    public String getAddress() {
        return address;
    }

    public String getAdditionalPreferences() {
        return additionalPreferences;
    }
}
