package model;



public class User {
    private long id;
    private String fullName;
    private String birthday;
    private String phoneNumber;
    private String address;
    private String email;
    private String userName;
    private String password;
    private Role role;
    private long creationTime;

    public User() {
    }

    public User(String raw) {
        String[] userInformation = raw.split("~");
        this.id = Integer.parseInt(userInformation[0]);
        this.fullName = userInformation[1];
        this.birthday = userInformation[2];
        this.phoneNumber = userInformation[3];
        this.address = userInformation[4];
        this.email = userInformation[5];
        this.userName = userInformation[6];
        this.password = userInformation[7];
        this.role = Role.parseRole(userInformation[8]);
        this.creationTime = Long.parseLong(userInformation[9]);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getFirstName() {
        int lastWhiteSpaceIndex = this.fullName.lastIndexOf(' ');
        if (lastWhiteSpaceIndex == -1) return this.fullName;
        return this.fullName.substring(lastWhiteSpaceIndex + 1);
    }

    public String getLastName() {
        int lastWhiteSpaceIndex = this.fullName.lastIndexOf(' ');
        if (lastWhiteSpaceIndex == -1) return "";
        return this.fullName.substring(0, lastWhiteSpaceIndex);

    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime() {
        this.creationTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return id +
                "~" + fullName +
                "~" + birthday +
                "~" + phoneNumber +
                "~" + address +
                "~" + email +
                "~" + userName +
                "~" + password +
                "~" + role +
                "~" + creationTime;
    }
}

