package jp.co.internous.ecsite.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.ecsite.model.dao.GoodsRepository;
import jp.co.internous.ecsite.model.dao.PurchaseRepository;
import jp.co.internous.ecsite.model.dao.UserRepository;
import jp.co.internous.ecsite.model.dto.HistoryDto;
import jp.co.internous.ecsite.model.dto.LoginDto;
import jp.co.internous.ecsite.model.entity.Goods;
import jp.co.internous.ecsite.model.entity.Purchase;
import jp.co.internous.ecsite.model.entity.User;
import jp.co.internous.ecsite.model.form.CartForm;
import jp.co.internous.ecsite.model.form.HistoryForm;
import jp.co.internous.ecsite.model.form.Loginform;

@Controller
@RequestMapping("/ecsite")

public class IndexController {

	@Autowired
	private GoodsRepository goodsRepos;
	
	@Autowired
	private UserRepository userRepos;
	
//	@Autowiredsd
	
	private Gson gson = new Gson(); //webサービスを使えるようにする
	
	@Autowired
	private PurchaseRepository purchaseRepos;
	
	@RequestMapping("/")
	public String index(Model m) {
		List<Goods> goods = goodsRepos.findAll();
		m.addAttribute("goods", goods);
		
		return "index";
	}
	
	@ResponseBody
	@PostMapping("/api/login")
	public String loginApi(@RequestBody Loginform form) { //Loginformのフィールドに渡される
		List<User> users = userRepos.findByUserNameAndPassword(form.getUserName(), form.getPassword());
		 
		LoginDto dto = new LoginDto(0, null, null, "ゲスト");
		System.out.println("ssss");
		if(users.size() > 0) {
			dto = new LoginDto(users.get(0));
			System.out.println(dto.getFullName());
		}
		return gson.toJson(dto);
	}
	
	@ResponseBody
	@PostMapping("/api/purchase")
	public String purchaseApi(@RequestBody CartForm f) {
		
		f.getCartList().forEach((c) -> {
			long total = c.getPrice() * c.getCount();
			purchaseRepos.persist(f.getUserId(), c.getId(), c.getGoodsName(), c.getCount(), total);
 		});
		return String.valueOf(f.getCartList().size());
	}
	
	@ResponseBody
	@PostMapping("/api/history")
	public String historyApi(@RequestBody HistoryForm form) { //@ResuestBody -> postからの情報をJSONで受け取る
		
		String userId = form.getUserId();
		List<Purchase> history = purchaseRepos.findHistory(Long.parseLong(userId));
		List<HistoryDto> historyDtoList = new ArrayList<>();
		history.forEach((v) ->{
			HistoryDto dto = new HistoryDto(v);
			historyDtoList.add(dto);
		});
//		System.out.println("AA");
		return gson.toJson(historyDtoList);
	}
}
