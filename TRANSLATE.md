# Translate Light Overlay to your language!

We welcome contributions to translate Light Overlay into different languages!
If you'd like to help, you can follow these steps:

### Step 1: Fork the repository on GitHub.

You can [click this link](https://github.com/lugosieben/lightoverlay/fork), leave the default settings, and click "Create fork". While you're at it, why not give the project a **star** aswell?

### Step 2: Check the language files.

Go into the `src/main/resources/assets/light-overlay/lang` folder and check if your language already exists.
If it already exists, skip Step 3.

### Step 3: Create a new language file.

Create a new file in the lang folder.
Name the file like the [Minecraft ingame locale codes](https://minecraft.wiki/w/Language) (e.g., `es_es.json` for Spanish (Spain), `fr_fr.json` for French (France), etc.).

### Step 4: Translate the strings.

To edit the file, you can click the pencil icon in GitHub to open the file editor.

If you created a new language file, you can copy the content of the `en_us.json` file into your new file as a starting point.  
If you want to update an existing language, you can see the exact missing strings by clicking on the topmost entry from [this table](https://github.com/lugosieben/lightoverlay/actions/workflows/lang-check.yml) and looking at the "Missing Translation Strings" entries.

**Never** change the keys (the strings before the colons `:`). They are the internal identifiers and need to stay the same so that the mod can find the correct translation.

### Step 5: Commit your changes and create a pull request.

Once you have finished translating, click on `Commit Changes` in the top right. You can optionally name your commit, then click on `Commit Changes` in the popup.

Go back to the main page of your fork (click on lightoverlay in the path), then click on `Contribute` and `Open Pull Request`.

Fill in a title and small description, then click on `Create Pull Request`.

### Thank you for contributing! ♥️
