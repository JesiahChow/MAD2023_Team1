package sg.edu.np.mad.madassignmentteam1.utilities;

public class StringUtility
{
    public static final StringUtility instance = new StringUtility();

    private StringUtility()
    {

    }

    public String getAsCapitalizedString(String string)
    {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }
}
