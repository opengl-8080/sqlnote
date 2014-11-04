package sqlnote.domain

interface SystemConfigurationRepository {

    SystemConfiguration find();
    void modify(SystemConfiguration config);
}
