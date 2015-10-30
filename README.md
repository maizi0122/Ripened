# Maizi-Android
The open-source project of Maizi-studio

Module ViewInjection -- an lightly automatic view injection library of android

  1.download the source-bundle at right of the webpage.
  2.import this library into your gradle project.
  3.edit your app module with build.gradle like this:

  dependencies{
      //-----add library module like this-----
      compile project(':viewinjection')
      //--------------------------------------


  4.edit your subclass of Activity like this:
  public class MainActivity extends Activity {

      //--add annotation ResId like this--
      @ResId(id = R.id.maizi)
      private LinearLayout maizi;
      //--define your view field like this--

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);

          setContentView(R.layout.activity_main);
          //--invoke auto view injection like this--
          new ViewInjection().initView(this);

  5.enjoy your coding-time...