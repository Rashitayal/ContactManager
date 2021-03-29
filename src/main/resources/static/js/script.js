console.log("esfswf");

$('#file-upload').change(function() {
	console.log("esfsfs");
  var file = $('#file-upload')[0].files[0].name;
console.log(file);
  $("#label-image").html(file);
});

const search=()=>{
	
	let query = $("#search-input").val();
	if(query == ""){
		$(".search-result").hide();
	} else{
		let url = `http://localhost:8080/contacts/${query}`;
		fetch(url).then((response) => {
			return response.json();
		}).then((data) => {
			let text = `<div class='list-group'>`;
			data.forEach((contact) => {
				text+=`<a href='/user/viewContact/${contact.contactId}' class='list-group-item list-group-action'> ${contact.firstName + ' ' + contact.lastName}</a>`
			});
			text+=`</div>`;
			$(".search-result").html(text);
			$(".search-result").show();
		});	
	}
	
}


