package features.nami;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.nami.annotation.NamiClient;
import org.noear.solon.test.SolonJUnit4ClassRunner;

/**
 * @author noear 2022/11/7 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
public class NamiDefaultTest {

    @NamiClient(url = "https://api.github.com")
    GitHub gitHub;

    @Test
    public void test() {
        assert gitHub.hashCode() > 0;
        assert "hello".equals(gitHub.hello());
    }
}
