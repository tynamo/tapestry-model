var helpShown = false;

function toggleHelp()
{
	helpShown = !helpShown;
	
	var images = document.getElementsByTagName("img");
	for (var i = 0; i < images.length; i++)
	{
		var img = images.item(i);
		if (img.src.indexOf("resource/help.gif") != -1)
		{
			if (helpShown)
			{
				img.style.display = "inline";
			}
			else
			{
				img.style.display = "none";
			}
		}
	}
}
