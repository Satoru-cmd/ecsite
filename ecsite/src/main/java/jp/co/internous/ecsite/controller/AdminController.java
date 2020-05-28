package jp.co.internous.ecsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.internous.ecsite.model.dao.GoodsRepository;
import jp.co.internous.ecsite.model.dao.UserRepository;
import jp.co.internous.ecsite.model.entity.Goods;
import jp.co.internous.ecsite.model.entity.User;
import jp.co.internous.ecsite.model.form.Loginform;

@Controller
@RequestMapping("/ecsite/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepos;

	@Autowired
	private GoodsRepository goodsRepos;
	@RequestMapping("/")
	public String index() {
		return "adminindex";
	}

	@RequestMapping("/welcome")
	public String welcome(Loginform form, Model m) { //Loginformのフィールドに渡される
		List<User> users = userRepos.findByUserNameAndPassword(form.getUserName(), form.getPassword());

		if(users != null && users.size() > 0) {
			boolean isAdmin = users.get(0).getIsAdmin() != 0; //1 != 0 でtrueが帰る

			if(isAdmin) { //(isAdmin == true)
				List<Goods> goods = goodsRepos.findAll(); //findAll()の戻り値はリスト
				m.addAttribute("userName", users.get(0).getUserName());
				m.addAttribute("password", users.get(0).getPassword());
				m.addAttribute("goods", goods);
			}
		}
//		System.out.println(form.getUserName() + " " + form.getPassword());

		return "welcome";
	}
}




















