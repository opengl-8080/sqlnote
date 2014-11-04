package sqlnote.domain.system

interface SystemConfigurationRepository {

    SystemConfiguration find();
    void modify(SystemConfiguration config);
}
