package cn.com.easy.dubbo;

/**
 * 用户服务接口实现类
 * 
 * @author nibili 2015年11月11日
 * 
 */
public class UserServiceImpl implements IUserService {

	@Override
	public String getUserById(Long userId) {
		if (userId == 1) {
			return "毛泽东";
		} else if (userId == 2) {
			return "周恩来";
		} else if (userId == 3) {
			return "叶剑英";
		} else {
			return "查无此人";
		}
	}

}
