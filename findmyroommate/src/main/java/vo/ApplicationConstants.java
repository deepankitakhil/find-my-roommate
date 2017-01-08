package vo;

/**
 * Created by akhil on 1/7/2017.
 */

public enum ApplicationConstants {

    APPLICATION_DB_ROOT_REFERENCE("user"),
    APPLICATION_PACKAGE_NAME("com.akhil.findmyroommate");

    private String value;

    ApplicationConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
