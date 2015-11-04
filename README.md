# Maizi-Android
The open-source project of Maizi-studio

Module ViewInjection -- a lightly automatic view injection and smart listener binding library of android(it is a beginning project...)

  1.download the source-bundle at right of the webpage.

  2.import this library into your gradle project.

  3.edit your app module with build.gradle like this:

  dependencies {

      //-----add library module like this-----
      compile project(':viewinjection')
      //--------------------------------------

  4.1 function of auto view injection:

      edit your subclass of Activity like this:

      public class MainActivity extends Activity {

          //--define your view field like this--
          @ResId(id = R.id.ac_main_bt1)
          private Button ac_main_bt1;
          //--define your view field like this--

          @Override
          protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);

              setContentView(R.layout.activity_main);
              //--invoke auto view injection like this--
              new ViewInjection().initView(this);
              //why not static-method? it will be product by the bean-factory of maizi-studio open source project at soon.like spring...
          }
      }

      edit your subclass of Fragment like this:
      public class Fragment_Maizi extends android.app.Fragment {

          @ResId(id = R.id.ac_main_frag1_bt1)
          private Button ac_main_frag1_bt1;

          public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                  View root = inflater.inflate(R.layout.layout_fragment_1, container, false);
                  //--invoke auto view injection like this--
                  new ViewInjection().initView(this, root);
                  //----------------------------------------
                  return root;
              }
      }

  4.2 function of smart listener binding.

      if you abide by the rule of naming the set-method for one field of your class, which follows with the rule of android-source project.<br />
      when your field's type is an interface which have only one method, your set-method should be named like setXxxYyy<br />
      when your field's type is an interface which have more than one method, your set-method should be named like addXxxYyy<br />
      such as:
      public class A{
          private Xxx abc;

          public void setAbc(Xxx abc){
                this.abc = abc;
            }
          //the interface which has only one function(),it's "set-method" start with "set"
          public interface Xxx{
                return-type fun(params...);
          }

          private Yyy def;

          public void addDef(Yyy def){
                this.def = def;
          }
          //the interface which has more than one function(),it's "set-method" start with "add"
          public interface Yyy{
                return-type fun1(params...);
                return-type fun2(params...);
                .
                .
          }
      }

      if you setup this object, we can help you bind the listener smartly.

      In fact, we are not force you to use auto-event binding, you can bind the listener all by yourself.

      we have many ways to bind the listener.example below:

      **********************************************************************************************

      public class CustomOnClickListener1 implements View.OnClickListener {

          @Override
          @EventTarget(targets = {R.id.ac_main_bt5,R.id.ac_main_frag1_bt5})
          public void onClick(View v) {
              Toast.makeText(v.getContext(), "方式五被点击了.....", Toast.LENGTH_SHORT).show();
          }

      }
      ============================================================================================
      public class CustomOnClickListener2 implements View.OnClickListener {

          private String text;

          public CustomOnClickListener2(String text) {
              this.text = text;
          }

          @Override
          @EventTarget(targets = {R.id.ac_main_bt6, R.id.ac_main_frag1_bt6})
          public void onClick(View v) {
              Toast.makeText(v.getContext(), "方式六被点击了.....", Toast.LENGTH_SHORT).show();
          }
      }

      ************************************Activity like this****************************************

      edit your subclass of Activity like this:

      public class MainActivity extends Activity implements View.OnClickListener {

          //--define your view field like this--
          @ResId(id = R.id.ac_main_bt1)
          @RegistListener(listeners = {MainActivity.class})
          private Button ac_main_bt1;
          //--and add annotation ResId like this--

          @ResId(id = R.id.ac_main_bt2)
          @RegistListener(listeners = {MyOnClickListener2.class})
          private Button ac_main_bt2;

          @ResId(id = R.id.ac_main_bt3)
          @RegistListener(listeners = {MyOnClickListener3.class})
          private Button ac_main_bt3;

          @ResId(id = R.id.ac_main_bt4)
          @RegistListener(listeners = {MyOnClickListener4.class})
          private Button ac_main_bt4;

          @ResId(id = R.id.ac_main_bt5)
          @RegistListener(listeners = {CustomOnClickListener1.class})
          private Button ac_main_bt5;

          @ResId(id = R.id.ac_main_bt6)
          @RegistListener(listeners = {CustomOnClickListener2.class})
          private Button ac_main_bt6;

          @ResId(id = R.id.ac_main_bt7)
          private Button ac_main_bt7;

          @Override
          protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);

              setContentView(R.layout.activity_main);
              //--invoke auto view injection like this--
              new ViewInjection(new EventBinder()).initView(this, new MyOnClickListener4("方式四被点击了....."), new CustomOnClickListener2("方式六被点击了....."));

              ac_main_bt7.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Toast.makeText(MainActivity.this, "方式七被点击了.....", Toast.LENGTH_SHORT).show();
                  }
              });

          }

          @Override
          @EventTarget(targets = {R.id.ac_main_bt1})
          public void onClick(View v) {
              Toast.makeText(this, "方式一被点击了.....", Toast.LENGTH_SHORT).show();
          }

          private class MyOnClickListener2 implements View.OnClickListener {

              @Override
              @EventTarget(targets = {R.id.ac_main_bt2})
              public void onClick(View v) {
                  Toast.makeText(MainActivity.this, "方式二被点击了.....", Toast.LENGTH_SHORT).show();
              }
          }

          private class MyOnClickListener3 extends CustomOnClickListener1 {

              @Override
              @EventTarget(targets = {R.id.ac_main_bt3})
              public void onClick(View v) {
                  Toast.makeText(MainActivity.this, "方式三被点击了.....", Toast.LENGTH_SHORT).show();
              }
          }

          private class MyOnClickListener4 implements View.OnClickListener {

              private String text;

              public MyOnClickListener4(String text) {
                  this.text = text;
              }

              @Override
              @EventTarget(targets = {R.id.ac_main_bt4})
              public void onClick(View v) {
                  Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
              }
          }

      }

      **************************************fragment like this**************************************

      public class MainActivity extends Activity {

          @ResId(id = R.id.maizi_contaniner)
          private RelativeLayout maizi_contaniner;

          @Override
          protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);

              setContentView(R.layout.activity_main);
              //--invoke auto view injection like this--
              new ViewInjection().initView(this);

              FragmentManager fragmentManager = getFragmentManager();
              FragmentTransaction ft = fragmentManager.beginTransaction();
              ft.replace(R.id.maizi_contaniner, new Fragment_Maizi());
              ft.commit();
          }
      }

      edit your subclass of Fragment like this:

      public class Fragment_Maizi extends android.app.Fragment implements View.OnClickListener {

          @ResId(id = R.id.ac_main_frag1_bt1)
          @RegistListener(listeners = {Fragment_Maizi.class})
          private Button ac_main_frag1_bt1;

          @ResId(id = R.id.ac_main_frag1_bt2)
          @RegistListener(listeners = {MyOnClickListener2.class})
          private Button ac_main_frag1_bt2;

          @ResId(id = R.id.ac_main_frag1_bt3)
          @RegistListener(listeners = {MyOnClickListener3.class})
          private Button ac_main_frag1_bt3;

          @ResId(id = R.id.ac_main_frag1_bt4)
          @RegistListener(listeners = {MyOnClickListener4.class})
          private Button ac_main_frag1_bt4;

          @ResId(id = R.id.ac_main_frag1_bt5)
          @RegistListener(listeners = {CustomOnClickListener1.class})
          private Button ac_main_frag1_bt5;

          @ResId(id = R.id.ac_main_frag1_bt6)
          @RegistListener(listeners = {CustomOnClickListener2.class})
          private Button ac_main_frag1_bt6;

          @ResId(id = R.id.ac_main_frag1_bt7)
          private Button ac_main_frag1_bt7;

          @Nullable
          @Override
          public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

              View root = inflater.inflate(R.layout.layout_fragment_1, container, false);

              new ViewInjection(new EventBinder()).initView(this, root, new MyOnClickListener4("方式四被点击了....."), new CustomOnClickListener2("方式六被点击了....."));

              ac_main_frag1_bt7.setOnClickListener(new View.OnClickListener() {

                  @Override
                  public void onClick(View v) {
                      Toast.makeText(Fragment_Maizi.this.getActivity(), "方式七被点击了.....", Toast.LENGTH_SHORT).show();
                  }
              });
              return root;
          }

          @Override
          @EventTarget(targets = {R.id.ac_main_frag1_bt1})
          public void onClick(View v) {
              Toast.makeText(this.getActivity(), "方式一被点击了.....", Toast.LENGTH_SHORT).show();
          }

          private class MyOnClickListener2 implements View.OnClickListener {

              @Override
              @EventTarget(targets = {R.id.ac_main_frag1_bt2})
              public void onClick(View v) {
                  Toast.makeText(Fragment_Maizi.this.getActivity(), "方式二被点击了.....", Toast.LENGTH_SHORT).show();
              }
          }

          private class MyOnClickListener3 extends CustomOnClickListener1 {

              @Override
              @EventTarget(targets = {R.id.ac_main_frag1_bt3})
              public void onClick(View v) {
                  Toast.makeText(Fragment_Maizi.this.getActivity(), "方式三被点击了.....", Toast.LENGTH_SHORT).show();
              }
          }

          private class MyOnClickListener4 implements View.OnClickListener {

              private String text;

              public MyOnClickListener4(String text) {
                  this.text = text;
              }

              @Override
              @EventTarget(targets = {R.id.ac_main_frag1_bt4})
              public void onClick(View v) {
                  Toast.makeText(Fragment_Maizi.this.getActivity(), text, Toast.LENGTH_SHORT).show();
              }
          }
      }

  5.enjoy your coding-time...