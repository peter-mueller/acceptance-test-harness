package plugins;

import org.jenkinsci.test.acceptance.junit.AbstractJUnitTest;
import org.jenkinsci.test.acceptance.junit.WithPlugins;
import org.jenkinsci.test.acceptance.po.FreeStyleJob;
import org.jenkinsci.test.acceptance.po.Jenkins;
import org.jenkinsci.test.acceptance.po.JenkinsLogger;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.jenkinsci.test.acceptance.po.PageObject.*;

/**
 * Test audit_trail plugin
 */
@WithPlugins("audit-trail")
public class AuditTrailPluginTest extends AbstractJUnitTest {
    @Inject
    Jenkins jenkins;

    JenkinsLogger auditTrail;

    @Before
    public void setUp() {
        auditTrail = jenkins.getLogger("Audit Trail");
    }


    /**
     * Scenario: Trail should be empty after installation
     *   Given I have set up the Audit Trail plugin
     *   Then the audit trail should be empty
     */
    @Test
    public void trail_should_be_empty_after_login() {
        assertThat(auditTrail.isEmpty(), is(true));
    }

    /**
     * Scenario: Trail should contain logged events
     *   Given I have set up the Audit Trail plugin
     *   When I create a job named "job"
     *   And  I create dumb slave named "slave"
     *   Then the audit trail should contain event "/createItem"
     *   And  the audit trail should contain event "/computer/createItem"
     */
    @Test
    public void trail_should_contain_logged_events() {
        jenkins.createJob(FreeStyleJob.class);
        jenkins.createDumbSlave(createRandomName());

        List<String> events = auditTrail.getEvents();
        assertThat(events, hasItem("/createItem"));
        assertThat(events, hasItem("/computer/createItem"));
    }
}