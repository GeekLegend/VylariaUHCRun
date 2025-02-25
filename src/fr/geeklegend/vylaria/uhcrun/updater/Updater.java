package fr.geeklegend.vylaria.uhcrun.updater;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.google.common.base.Joiner;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class Updater {

    private final Plugin plugin;
    private final int id;
    private final File pluginFile;
    private final boolean download;
    private final boolean announce;
    private final File updateFolder;
    private URL url;

    private final YamlConfiguration config = new YamlConfiguration();

    private Result result = Result.SUCCESS;
    private JsonObject updateData;
    private String response;
    private Thread updaterThread;

    private static final String Updater_VERSION = "0.1";

    /**
     * Represents the updater's result.
     */

    public enum Result {

        /**
         * A new version has been found, downloaded and will be loaded at the next server reload / restart.
         */

        SUCCESS,

        /**
         * A new version has been found but nothing was downloaded.
         */

        UPDATE_AVAILABLE,

        /**
         * No update found.
         */

        NO_UPDATE,

        /**
         * The updater is disabled.
         */

        DISABLED,

        /**
         * An error occurred.
         */

        ERROR
    }

    /**
     * Info type enumeration.
     */

    public enum InfoType {

        /**
         * Gets the download URL.
         */

        DOWNLOAD_URL("downloadUrl"),

        /**
         * Gets the file name.
         */

        FILE_NAME("fileName"),

        /**
         * Gets the game version.
         */

        GAME_VERSION("gameVersion"),

        /**
         * Gets the file's title.
         */

        FILE_TITLE("name"),

        /**
         * Gets the release type.
         */

        RELEASE_TYPE("releaseType");

        private final String jsonKey;

        InfoType(final String jsonKey) {
            this.jsonKey = jsonKey;
        }

        public final String getJSONKey() {
            return jsonKey;
        }

    }

    /**
     * Initializes Updater.
     *
     * @param plugin Your plugin.
     * @param id Your plugin ID on BukkitDev (you can get it here : https://api.curseforge.com/servermods/projects?search=your+plugin).
     * @param pluginFile The plugin file. You can get it from your plugin using <i>getFile()</i>.
     * @param download If you want to download the file.
     * @param announce If you want to announce the progress of the update.
     *
     * @throws IOException I/O error.
     * @throws InvalidConfigurationException If there is a problem with Updater's config.
     */

    public Updater(final Plugin plugin, final int id, final File pluginFile, final boolean download, final boolean announce) throws IOException, InvalidConfigurationException {
        this.plugin = plugin;
        this.id = id;
        this.pluginFile = pluginFile;
        this.download = download;
        this.announce = announce;

        updateFolder = Bukkit.getUpdateFolderFile();
        if(!updateFolder.exists()) {
            updateFolder.mkdir();
        }

        final File UpdaterFolder = new File(plugin.getDataFolder().getParentFile(), "Updater");
        if(!UpdaterFolder.exists()) {
            UpdaterFolder.mkdir();
        }

        final String lineSeparator = System.lineSeparator();
        final StringBuilder header = new StringBuilder();
        header.append("What is Updater ?" + lineSeparator);
        header.append("Updater is a simple updater created by Vylaria It aims to auto-update Bukkit Plugins." + lineSeparator + lineSeparator);
        header.append("What happens during the update process ?" + lineSeparator);
        header.append("1. Connection to curseforge.com." + lineSeparator);
        header.append("2. Plugin version compared against version on curseforge.com." + lineSeparator);
        header.append("3. Downloading of the plugin from curseforge.com if a newer version is found." + lineSeparator + lineSeparator);
        header.append("So what is this file ?" + lineSeparator);
        header.append("This file is just a config file for this auto-updater." + lineSeparator + lineSeparator);
        header.append("Configuration :" + lineSeparator);
        header.append("'enable': Choose if you want to enable the auto-updater." + lineSeparator);
        header.append("'api-key': OPTIONAL. Your BukkitDev API Key." + lineSeparator + lineSeparator);
        header.append("Good game, I hope you will enjoy your plugins always up-to-date ;)" + lineSeparator);

        final File configFile = new File(UpdaterFolder, "updater.yml");
        if(!configFile.exists()) {
            configFile.createNewFile();
            config.options().header(header.toString());
            config.set("enable", true);
            config.set("api-key", "NONE");
            config.save(configFile);
        }
        config.load(configFile);

        if(!config.getBoolean("enable")) {
            result = Result.DISABLED;
            log(Level.INFO, "Updater is disabled.");
            return;
        }

        url = new URL("https://api.curseforge.com/servermods/files?projectIds=" + id);
        updaterThread = new Thread(new UpdaterThread());
        updaterThread.start();
    }

    /**
     * Gets the version of Updater.
     *
     * @return The version of Updater.
     */

    public static String getVersion() {
        return Updater_VERSION;
    }

    /**
     * Gets the result of Updater.
     *
     * @return The result of the update process.
     */

    public final Result getResult() {
        waitForThread();
        return result;
    }

    /**
     * Gets information about the latest file.
     *
     * @param type The kind of information you want.
     *
     * @return The information you want.
     */

    public final String getLatestFileInfo(final InfoType type) {
        waitForThread();
        return updateData.get(type.getJSONKey()).asString();
    }

    /**
     * Gets raw data about the latest file.
     *
     * @return A JSON object which contains every of the update process.
     */

    public final JsonObject getLatestFileData() {
        waitForThread();
        return updateData;
    }

    /**
     * Downloads a file.
     *
     * @param site The URL of the file you want to download.
     * @param pathTo The path where you want the file to be downloaded.
     *
     * @return <b>true</b>If the download was a success.
     * </b>false</b>If there is an error during the download.
     */

    private boolean download(final String site, final File pathTo) {
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL(site).openConnection();
            connection.addRequestProperty("User-Agent", "Updater v" + Updater_VERSION);

            response = connection.getResponseCode() + " " + connection.getResponseMessage();
            if(!response.startsWith("2")) {
                log(Level.WARNING, "Bad response : '" + response + "' when trying to download the update.");
                result = Result.ERROR;
                return false;
            }

            final byte[] data = new byte[1024];
            final long size = connection.getContentLengthLong();
            final long koSize = size / 1000;

            final InputStream inputStream = connection.getInputStream();
            final FileOutputStream fileOutputStream = new FileOutputStream(pathTo);
            final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 1024);

            int lastPercent = 0;
            float totalDataRead = 0;
            int i;

            while((i = inputStream.read(data, 0, 1024)) >= 0) {
                totalDataRead += i;
                bufferedOutputStream.write(data, 0, i);

                if(announce) {
                    final int percent = (int)((long)(totalDataRead * 100) / size);
                    if(lastPercent != percent) {
                        lastPercent = percent;
                        log(Level.INFO, percent + "% of " + koSize + "ko...");
                    }
                }
            }

            bufferedOutputStream.close();
            fileOutputStream.close();
            inputStream.close();
            return true;
        }
        catch(final Exception ex) {
            log(Level.SEVERE, "Exception '" + ex + "' occurred while downloading update. Please check your network connection.");
            result = Result.ERROR;
        }
        return false;
    }

    /**
     * Compares two versions.
     *
     * @param versionTo The version you want to compare to.
     * @param versionWith The version you want to compare with.
     *
     * @return <b>true</b> If <b>versionTo</b> is inferior than <b>versionWith</b>.
     * <br><b>false</b> If <b>versionTo</b> is superior or equals to <b>versionWith</b>.
     */

    public static boolean compareVersions(final String versionTo, final String versionWith) {
        return normalisedVersion(versionTo, ".", 4).compareTo(normalisedVersion(versionWith, ".", 4)) > 0;
    }

    /**
     * Gets the formatted name of a version.
     * <br>Used for the method <b>compareVersions(...)</b> of this class.
     *
     * @param version The version you want to addLocation.
     * @param separator The separator between the numbers of this version.
     * @param maxWidth The max width of the formatted version.
     *
     * @return The formatted version of your version.
     *
     * @author Peter Lawrey.
     */

    private static String normalisedVersion(final String version, final String separator, final int maxWidth) {
        final StringBuilder stringBuilder = new StringBuilder();
        for(final String normalised : Pattern.compile(separator, Pattern.LITERAL).split(version)) {
            stringBuilder.append(String.format("%" + maxWidth + 's', normalised));
        }
        return stringBuilder.toString();
    }

    /**
     * Logs a message if "announce" is set to true.
     *
     * @param level The level of logging.
     * @param message The message.
     */

    private void log(final Level level, final String message) {
        if(announce) {
            Bukkit.getLogger().log(level, "[Updater] " + message);
        }
    }

    /**
     * As the result of Updater output depends on the thread's completion,
     * <br>it is necessary to wait for the thread to finish before allowing anyone to check the result.
     *
     * @author <b>Gravity</b> from his Updater.
     */

    private void waitForThread() {
        if(updaterThread != null && updaterThread.isAlive()) {
            try {
                updaterThread.join();
            }
            catch(InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class UpdaterThread implements Runnable {

        @Override
        public void run() {
            try {
                final String pluginName = plugin.getName().replace("_", " ");
                final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.addRequestProperty("User-Agent", "Updater v" + Updater_VERSION);

                final String apiKey = config.getString("api-key");
                if(apiKey != null && !apiKey.equals("NONE")) {
                    connection.addRequestProperty("X-API-Key", apiKey);
                }
                response = connection.getResponseCode() + " " + connection.getResponseMessage();
                if(!response.startsWith("2")) {
                    log(Level.INFO, "Bad response : '" + response + (response.startsWith("402") ? "'. Maybe your API Key is invalid ?" : "'."));
                    result = Result.ERROR;
                    return;
                }

                final InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                final String response = bufferedReader.readLine();

                if(response != null && !response.equals("[]")) {
                    final JsonArray jsonArray = Json.parse(response).asArray();
                    updateData = jsonArray.get(jsonArray.size() - 1).asObject();

                    if(compareVersions(getLatestFileInfo(InfoType.FILE_TITLE).split("^v|[\\s_-]v")[1].split(" ")[0], plugin.getDescription().getVersion()) && getLatestFileInfo(InfoType.DOWNLOAD_URL).toLowerCase().endsWith(".jar")) {
                        result = Result.UPDATE_AVAILABLE;

                        if(download) {
                            log(Level.INFO, "Downloading a new update : " + getLatestFileInfo(InfoType.FILE_TITLE) + "...");

                            if(download(getLatestFileInfo(InfoType.DOWNLOAD_URL), new File(updateFolder, pluginFile.getName()))) {
                                result = Result.SUCCESS;
                                log(Level.INFO, "The update of '" + pluginName + "' has been downloaded and installed. It will be loaded at the next server load / reload.");
                            }
                            else {
                                result = Result.ERROR;
                            }

                        }
                        else {
                            log(Level.INFO, "An update has been found for '" + pluginName + "' but nothing was downloaded.");
                        }
                        return;

                    }
                    else {
                        result = Result.NO_UPDATE;
                        log(Level.INFO, "No update found for '" + pluginName + "'.");
                    }

                }
                else {
                    log(Level.SEVERE, "The ID '" + id + "' was not found (or no files found for this project) ! Maybe the author(s) (" + Joiner.on(", ").join(plugin.getDescription().getAuthors()) + ") of '" + pluginName + "' has/have misconfigured his/their plugin ?");
                    result = Result.ERROR;
                }

                bufferedReader.close();
                inputStreamReader.close();
            }
            catch(final Exception ex) {
                log(Level.SEVERE, "Exception '" + ex + "'. Please check your network connection.");
                result = Result.ERROR;
            }
        }
    }

}
