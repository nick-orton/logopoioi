function focusIf(selector){
  if($(selector).length != 0){
    $(selector).focus();
  } 
}

function addTagCloud(){
  var tc_div = $("#tag_cloud");
  if(tc_div.length !=0){
    $.getJSON('/all_tags', function(data){
      $.each(data[0], function(k,v){
        console.log(k,v); 
        tc_div.append("<a class='bangtag_link' href='/bangtag/"+k.substring(1,k.length)+"'>" + k + "</a>");
      });
    });
  }
}

$(document).ready(function(){
  focusIf("#the_box");
  focusIf("#pwd_input");

  $("#save").click(function(event){
    $("#the_form").submit();      
  });

  addTagCloud();

});


//Take from http://css-tricks.com/better-password-inputs-iphone-style/
$(function() {
  $("#login_form div").append("<div id='letterViewer'>");
  $("#pwd_input").keypress(function(e) {
    $("#letterViewer")
      .html(String.fromCharCode(e.which))
      .fadeIn(200, function() {
          $(this).fadeOut(200);
       });
  });
});
