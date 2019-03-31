# ATTENTION

This plugin is an early alpha.

Many functions not implemented, like:

- Attachments
- Comments editing
- Dragging-and-dropping tickets in the Query view to move them between columns
- And other...

Some functions don't work as expected - you find and tell [me](mailto:alecn2002@gmail.com)

# Glo Boards issue tracking plugin for NetBeans [![][license img]][license]

## Preword

This plugin was checked for compatibility with NetBeans 8.2 and Java JRE 8. Though it should work with later NetBeans releases, and later Java.

## Obtaining Auth Key

This plugin supports GitKraken Personal Access Tokens (PAT) auth method only.

To obtain PAT:

* Login to your [GitKraken App account](https://app.gitkraken.com/)
* Click on the user menu (the one where your login name is shown) in the upper right corner of the screen
* Choose Personal Access Tokens from the failed down menu
* Generate access token on the appeared screen 
* **ATTENTION!** PAT is shown once only, at the time of PAT generation. Subsequent PAT generation invalidates previous one. Save your PAT in safe, reliable place!

## Building plugin

If you decide to build this plugin from sources, you will need:

- JDK 8+
- Maven 3.16+

To build plugin, just cd to it's root directory and issue command:

```
    mvn clean:clean install:install
```

You may find built plugin in the sub-dir target/nbm/ of the project directory

## Installing plugin

If you decided to build it yourself, as described in the previous part - open Tools->Plugins menu, go to Downloaded tab, press Add Plugins... and provide path to plugin described above.
The plugin will appear in the list of available plugins, proceed with common installation procedure.

If you install plugin from the NetBeans plugins repository, update the list of plugins and find it on the Available Plugins tab of Plugins window.

## Setting up plugin

After installation the Glo type will appear in 2 places in NetBeans interface:

1. Task Repositories entry on the Services tab
2. Tasks window

In both places you may start adding Glo Boards NB connector by choosing it in the list of available commectors.

Steps to create connector:

* Provide connector name. It's reasonable to use something like Glo-_BoardName_, see note below
* Copy-paste your PAT obtained on GitKraken Web page
* Press "Verify" button. In addition to verification, this action will fetch the list of Glo boards available to you.
* Choose board from dropdown
* Press OK

Please note that connector works with one board only. You need to create a connector per board if you plan to work with multiple boards.

