import com.thoughtworks.pos.domains.User;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by 5Wenbin on 2016/6/29.
 */
public class UserTest {
    private User user0 = new User("USER0001", "老人", true, 50);
    private User user1 = new User("USER0002", "小孩", false, 50);

    @Test
    public void vipAddScore(){
        assertThat(user0.getScore(), is(50));
        user0.addScore(20);
        assertThat(user0.getScore(), is(70));
    }

    @Test
    public void notVipAddScore(){
        assertThat(user1.getScore(), is(50));
        user1.addScore(20);
        assertThat(user1.getScore(), is(50));
    }

    @Test
    public void setVipForUserWhoHasIllegalScore(){
        user1.setIsVip(false);
        assertThat(user1.getScore(), is(50));
        assertThat(user1.getIsVip(), is(false));
        assertThat(user1.getScore(), is(50));
    }
}
