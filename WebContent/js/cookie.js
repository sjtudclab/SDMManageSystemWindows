function setCookie(cname,cvalue,exdays)
{
  var d = new Date();
  d.setTime(d.getTime()+(exdays*24*60*60*1000));
  var expires = "expires="+d.toGMTString();
  document.cookie = cname + "=" + cvalue + "; " + expires+"; path=/";
}


function getCookie(name)
{
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg))
		return unescape(arr[2]);
	else
		return "";
}


function checkCookie()
{
  var username=getCookie("username");
  if (username!="")
  {
    return true;
  }
  else 
  {
    return false;
  }
}

function delCookie(name){  
	setCookie(name,"",-1);
}  
