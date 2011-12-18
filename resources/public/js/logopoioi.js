function focusIf(selector){
  if($(selector).length != 0){
    $(selector).focus();
  } 
}

$(document).ready(function(){
  focusIf("#the_box");
  focusIf("#pwd_input");

  $("#save").click(function(event){
    $("#the_form").submit();      
  });
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
