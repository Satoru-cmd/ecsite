//let -> 変数宣言、再宣言禁止	
	let login = (event) => {
		event.preventDefault();
		let jsonString = {
				'userName' : $('input[name=userName]').val(), //入力された文字とペアにする ex)'userName : taro'
				'password' : $('input[name=password]').val()  //.val() -> valueの値を取得
		}; 
		$.ajax({
			type: 'POST',
			url: '/ecsite/api/login',		　// JSON -> 情報をやりとりするためのデータ形式(.json)
			data: JSON.stringify(jsonString), //JSON.stringify(引数) -> 引数をJSONにする
			contentType: 'application/json',
			datatype: 'json',
			scriptCharset: 'utf-8'
		})
		
		.then((result) => {
			let user = JSON.parse(result);
			$('#welcome').text(`-- ようこそ！ ${user.fullName} さん`);
			$('#hiddenUserId').val(user.id);	//.val(引数) -> 引数の値をvalue設定
			$('input[name=userName]').val('');
			$('input[name=password]').val('');
			
			}, () => {
				console.error('Error: ajax connection failed.');
			}
		);
	};
	
	let addCart = (event) => {
		let tdList = $(event.target).parent().parent().find('td'); //.target->イベント発生源; .parent()->親要素を取得; 
		//=>選択された<td>タグを取得
		//tdList[0] = id
		let id = $(tdList[0]).text();	//.text() ->テキストの取得
		let goodsName = $(tdList[1]).text();
		let price = $(tdList[2]).text();
		let count = $(tdList[3]).find('input').val();
		
		if(count === '0' || count === ''){
			alert('注文数が０または空欄です')
			return ;
		}
		
		let cart = {
				'id' : id,
				'goodsName' : goodsName,
				'price' : price,
				'count' : count
		};
		cartList.push(cart);
		
		let tbody = $('#cart').find('tbody');
		$(tbody).children().remove();
		cartList.forEach(function(cart, index){
			let tr = $('<tr />'); //tr -> 表の横方向の１行を定義
			
			$('<td />', {'text': cart.id}).appendTo(tr); //（追加する物).appendTo(追加する場所)
			$('<td />', {'text': cart.goodsName}).appendTo(tr);
			$('<td />', {'text': cart.price}).appendTo(tr);
			$('<td />', {'text': cart.count}).appendTo(tr);
			let tdButton = $('<td />');
			$('<button />',{
				'text': 'カート削除',
				'class': 'removeBtn',
			}).appendTo(tdButton);
			
			$(tdButton).appendTo(tr);
			$(tr).appendTo(tbody);
		});
		$('.removeBtn').on('click', removeCart);
	};
	
	let buy = (event) =>{
		$.ajax({
			type:'POST',
			url: '/ecsite/api/purchase',
			data: JSON.stringify({
				"userId": $('#hiddenUserId').val(),
				"cartList": cartList
			}),
			contentType: 'application/json',
			datatype:'json',
			scriptCharset: 'utf-8'
		})
		.then((result) => {
			alert('購入しました');
			},() => {
			console.error('Error: ajax connection failed');
			}
		);
	};
	
	let removeCart = (event) => {
		const tdList = $(event.target).parent().parent().find('td');
		let id = $(tdList[0]).text();
		cartList = cartList.filter(function(cart){	
			return cart.id !== id;
		});
		$(event.target).parent().parent().remove();
	};
	
	
	let showHistory = () =>{
//		alert($('#hiddenUserId').val());
		$.ajax({
			type:'POST',
			url: '/ecsite/api/history',
			data: JSON.stringify({"userId": $('#hiddenUserId').val() }),
			contentType: 'application/json',
			dataType: 'json',
			scriptCharset: 'utf-8'
		})
		.then((result) => {
//			let historyList = JSON.parse(result);
			let tbody = $('#historyTable').find('tbody');
			$(tbody).children().remove();
			result.forEach((history,index) => {
				let tr = $('<tr />');
				
				$('<td />', {'text': history.goodsName }).appendTo(tr);
				$('<td />', {'text': history.itemCount }).appendTo(tr);
				$('<td />', {'text': history.createdAt }).appendTo(tr);
				
				$(tr).appendTo(tbody);
			});
			$("#history").dialog("open");
		}, () => {
			console.error('Error: ajax connection failed');
		}
		);
	}
