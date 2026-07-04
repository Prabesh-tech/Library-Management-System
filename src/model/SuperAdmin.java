package model;

import config.AppConfig;

/**
 * SuperAdmin.java
 * Represents the highest privilege user who can override permissions and configure the system.
 */
public class SuperAdmin extends User {

    private static final long serialVersionUID = 1L;

    public SuperAdmin(String userId, String name, String email, String password,
                      String phone, String address) {
        super(userId, name, email, password, AppConfig.ROLE_SUPERADMIN, phone, address);
        setAccessLevel(AppConfig.ACCESS_SUPERADMIN);
    }

    public SuperAdmin(String userId, String name, String email, String password) {
        this(userId, name, email, password, "", "");
    }

    @Override
    public String getRoleDescription() {
        return "Super Admin – full system control, including global settings, user auditing, and database management.";
    }

    @Override
    public String getDashboardTitle() {
        return "Super Admin Dashboard – " + name + " | Full System Control";
    }

    public boolean canCreateAdmin() {
        return true;
    }

    public boolean canManagePermissions() {
        return true;
    }

    public boolean canManageBranches() {
        return true;
    }

    public boolean canConfigureSystem() {
        return true;
    }
}
