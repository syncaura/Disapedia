# Disapedia - A discord bot that allows you to search wikis!
##

### Todo:
* Add comments to all methods/classes
* Create some sort of deployment scripts for docker etc (Koen will be looking at this)
* Complete spec for how the commands will interact within the discord client
* Add admin permissions so that they can set defaults and modify the core functionality of the bot (Could make this based on roles... but would I need to define these in the config?)
* Look into way of stopping spam in channel when searching for a specific query
* Improve the way that commands are currently defined
* Consider how I can stop the spam of this bot.. how quick can users use this?
* Allow users to define there own messages via a JSON file (messages.json)
* Possibly base all of the settings on roles/permission levels from discord
* Look into caching user permissions etc... might make API calls better!

# Default Languages
To change the default language of Disapedia simply go into the generated settings.json and change the "defaultLanguage" under the "wiki" key. You must use a VALID code from something similar to this:
http://astroneer.gamepedia.com/api.php?action=query&meta=siteinfo&siprop=languages
TODO: Make this a link that is not "astroneer"