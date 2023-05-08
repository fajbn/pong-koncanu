import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Pong extends Application
{
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width=screenSize.getWidth();
    double height=screenSize.getHeight();
    int w=(int)width;
    int h=(int)height;

    private Stage stage;

    private boolean igraTece=false;
    private long prejsnjiCas=0;

    //spremeljivke za meni
    private Scene Meni;
    private Button gumbIgraj;
    private Button gumbIzhod;
    private Button gumbHitrost1;
    private Button gumbHitrost2;
    private Button gumbHitrost3;
    Text rekordTextMeni=new Text();
    Text tockeTextMeni=new Text();

    //spremenljivke za igro
    private Scene Igra;
    private Button gumbPavza;
    double xi=0;
    double yi=h-h/40;
    int speedx=5;
    int speedy=4;
    boolean smerx=true;
    boolean smery=true;
    int xw=w;
    int yw=h;
    int xz=w/3;
    int yz=h/3;
    int tocke=0;
    int rekord=0;
    Text tockeTextIgra=new Text();
    Text rekordTextIgra=new Text();
    Text novRekordText=new Text();
    Circle zoga=new Circle();
    Rectangle ploscek=new Rectangle();
    Rectangle zgornjaMeja=new Rectangle();
    public int nacin=0;

    //pravila za premikanje zoge
    public void update(long pretekliCas) throws IOException 
    {
        if (smerx==true)
        {
            xz+=speedx;
            zoga.setCenterX(xz);
        }

        if(smerx==false)
        {
            xz-=speedx;
            zoga.setCenterX(xz);
        }

        if(smery==true)
        {
            yz+=speedy;
            zoga.setCenterY(yz);
        }

        if(smery==false)
        {
            yz-=speedy;
            zoga.setCenterY(yz);
        }

        if(xz-w/192<=0) //zoga se odbije od levega roba ekrana
        {
            smerx=true;
        }

        if(xz+w/192>=w) //zoga se odbije od desnega roba ekrana
        {
            smerx=false;
        }
        
        if(yz-((h/40))<=h/8) // zoga se odbije od vrhnjega roba ekrana
        {
            smery=true;
        }

        if(yz+h/40>=h) //zoga se odbije od spodnjega roba wakna
        {
            smery=false;
            novRekordText.setVisible(false);
            if(tocke<=rekord)
            {
                tocke=0;
                tockeTextIgra.setText("TOČKE: "+tocke);
                tockeTextMeni.setText("TOČKE: "+tocke);
                shraniTocke();
            }
            if(tocke>rekord)
            {
                rekord=tocke;
                shraniRekord();
                tocke=0;
                tockeTextIgra.setText("TOČKE: "+tocke);
                tockeTextMeni.setText("TOČKE: "+tocke);
                rekordTextIgra.setText("REKORD: "+rekord);
                rekordTextMeni.setText("REKORD: "+rekord);
                shraniTocke();
            }
        }

        if(yz+h/108>=yi && xz>=xi && xz<=xi+w/15) //zoga se odbije od vrha loparja
        {
            smery=false;
            tocke++;
            if(tocke<=rekord)
            {
                tockeTextIgra.setText("TOČKE: "+tocke);
                tockeTextMeni.setText("TOČKE: "+tocke);
                shraniTocke();
            }
            if(tocke>rekord)
            {
                novRekordText.setVisible(true);
                tockeTextIgra.setText("TOČKE: "+tocke);
                tockeTextMeni.setText("TOČKE: "+tocke);
                shraniTocke();
            }
        }
    }

    private void renderFrame() {}
    
    public void start(Stage mainStage) throws IOException
    {
        stage = mainStage;
        
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); //prepreci d bi esc knof sou vn iz fullscreen

        //ustvari meni in game wakna
        Meni=ustvariMeni();
        Igra=ustvariIgro();

        //prebere shranjen rekord iz txt fila in ga napise 
        preberiRekord();
        preberiTocke();
        
        //da z tprvu waknu k se pokaze meni
        stage.setScene(Meni);
        stage.setFullScreen(true);
        stage.show();

        Meni.setCursor(Cursor.NONE);
        gumbIgraj.requestFocus();

        AnimationTimer timer=new AnimationTimer() 
        {
            @Override
            public void handle(long zdaj) 
            {
                if(!igraTece)
                {
                    prejsnjiCas=zdaj;
                    return;
                }

                long pretekliCas = zdaj - prejsnjiCas;
                prejsnjiCas=zdaj;

                try 
                {
                    update(pretekliCas);
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                }

                renderFrame();


            }
        };
        timer.start();
    }

    public Scene ustvariMeni() throws IOException
    {
        Pane paneMeni=new Pane();
        Scene Meni=new Scene(paneMeni, w, h);

        paneMeni.setStyle("-fx-background-color: black;");
        Font font = Font.font("Arial", FontWeight.BOLD, w/50);

        Image image=new Image("File:logo.png");
        ImageView logo=new ImageView(image);
        logo.setFitWidth(w/2);
        logo.setFitHeight(h/3);
        logo.setX(w/4);
        logo.setY(h/8);
        paneMeni.getChildren().add(logo);

        tockeTextMeni.setText("TOČKE: "+tocke);
        tockeTextMeni.setX(w/50);
        tockeTextMeni.setY(h-h/30);
        tockeTextMeni.setFill(Color.WHITE);
        tockeTextMeni.setFont(Font.font("Arial",w/30));

        rekordTextMeni.setText("REKORD: "+rekord);
        rekordTextMeni.setX(w-w/4.5);
        rekordTextMeni.setY(h-h/30);
        rekordTextMeni.setFill(Color.WHITE);
        rekordTextMeni.setFont(Font.font("Arial",w/30));

        gumbIgraj = new Button("IGRAJ");
        gumbIgraj.setOnAction(e -> 
        {
            igraTece=true;
            spremeniOkna(Igra);
            Igra.setCursor(Cursor.NONE);
        });
        gumbIgraj.setLayoutX(w/2-(w/5)/2);
        gumbIgraj.setLayoutY(h/2+(h/15));
        gumbIgraj.setPrefWidth(w/5);
        gumbIgraj.setPrefHeight(h/15);
        gumbIgraj.setFont(font);
        gumbIgraj.setStyle("-fx-background-insets: 0,100; ");
        gumbIgraj.requestFocus();

        gumbHitrost1 = new Button("HITROST ŽOGICE: 1");
        gumbHitrost1.setLayoutX(w/2-(w/4)/2);
        gumbHitrost1.setLayoutY(h/2+(h/15)+(h/10));
        gumbHitrost1.setPrefWidth(w/4);
        gumbHitrost1.setPrefHeight(h/15);
        gumbHitrost1.setFont(font);
        gumbHitrost1.setStyle("-fx-background-insets: 0,100; ");
        gumbHitrost1.setVisible(true);

        gumbHitrost2 = new Button("HITROST ŽOGICE: 2");
        gumbHitrost2.setLayoutX(w/2-(w/4)/2);
        gumbHitrost2.setLayoutY(h/2+(h/15)+(h/10));
        gumbHitrost2.setPrefWidth(w/4);
        gumbHitrost2.setPrefHeight(h/15);
        gumbHitrost2.setFont(font);
        gumbHitrost2.setStyle("-fx-background-insets: 0,100; ");
        gumbHitrost2.setVisible(false);

        gumbHitrost3 = new Button("HITROST ŽOGICE: 3");
        gumbHitrost3.setLayoutX(w/2-(w/4)/2);
        gumbHitrost3.setLayoutY(h/2+(h/15)+(h/10));
        gumbHitrost3.setPrefWidth(w/4);
        gumbHitrost3.setPrefHeight(h/15);
        gumbHitrost3.setFont(font);
        gumbHitrost3.setStyle("-fx-background-insets: 0,100; ");
        gumbHitrost3.setVisible(false);

        gumbHitrost1.setOnAction(e-> 
         {
            gumbHitrost1.setVisible(false);
            gumbHitrost2.setVisible(true);
            speedx=7;
            speedy=5;
        }); 

        gumbHitrost2.setOnAction(c-> 
        {
            gumbHitrost2.setVisible(false);
            gumbHitrost3.setVisible(true);
            speedx=9;
            speedy=7;
        }); 

        gumbHitrost3.setOnAction(g-> 
        {
            gumbHitrost3.setVisible(false);
            gumbHitrost1.setVisible(true);
            speedx=5;
            speedy=3;
            gumbHitrost1.requestFocus();
        }); 

        gumbIzhod = new Button("IZHOD");
        gumbIzhod.setOnAction(e -> Platform.exit());
        gumbIzhod.setLayoutX(w/2-(w/5)/2);
        gumbIzhod.setLayoutY(h/2+(h/15)+(h/5));
        gumbIzhod.setPrefWidth(w/5);
        gumbIzhod.setPrefHeight(h/15);
        gumbIzhod.setFont(font);
        gumbIzhod.setStyle("-fx-background-insets: 0,100; ");

        paneMeni.getChildren().add(tockeTextMeni);
        paneMeni.getChildren().add(rekordTextMeni);
        paneMeni.getChildren().add(gumbHitrost1);
        paneMeni.getChildren().add(gumbHitrost2);
        paneMeni.getChildren().add(gumbHitrost3);
        paneMeni.getChildren().add(gumbIgraj);
        paneMeni.getChildren().add(gumbIzhod);

        return Meni;
    }

    public Scene ustvariIgro()
    {
        Pane paneIgra=new Pane();
        Scene Igra=new Scene(paneIgra, w, h);

        paneIgra.setStyle("-fx-background-color: black;");

        zgornjaMeja.setX(0);
        zgornjaMeja.setY(h/8);
        zgornjaMeja.setWidth(w);
        zgornjaMeja.setHeight(h/108);
        zgornjaMeja.setFill(Color.WHITE);

        ploscek.setX(xi);
        ploscek.setY(yi);
        ploscek.setWidth(w/15);
        ploscek.setHeight(h/40);
        ploscek.setFill(Color.WHITE);

        tockeTextIgra.setText("TOČKE: "+tocke);
        tockeTextIgra.setX(w/50);
        tockeTextIgra.setY(h/12);
        tockeTextIgra.setFill(Color.WHITE);
        tockeTextIgra.setFont(Font.font("Arial",w/30));

        rekordTextIgra.setText("REKORD: "+rekord);
        rekordTextIgra.setX(w-w/4.5);
        rekordTextIgra.setY(h/12);
        rekordTextIgra.setFill(Color.WHITE);
        rekordTextIgra.setFont(Font.font("Arial",w/30));

        novRekordText.setText("NOV REKORD!");
        novRekordText.setX(w/2-w/8.65);
        novRekordText.setY(h/4);
        novRekordText.setFill(Color.WHITE);
        novRekordText.setFont(Font.font("Arial",w/30));
        novRekordText.setVisible(false);

        zoga.setCenterX(xz);
        zoga.setCenterY(yz);
        zoga.setRadius(h/54);
        zoga.setFill(Color.RED);
        
        gumbPavza = new Button("PAVZA");
        gumbPavza.setLayoutX(w/2-(w/10));
        gumbPavza.setLayoutY(h/35);
        gumbPavza.setPrefWidth(w/5);
        gumbPavza.setPrefHeight(h/15);
        gumbPavza.setFont(Font.font("Arial", FontWeight.BOLD, w/50));
        gumbPavza.setStyle("-fx-background-insets: 0,100; ");
        gumbPavza.setOnAction(e ->
        {
            igraTece=false;
            spremeniOkna(Meni);
        });
        paneIgra.getChildren().add(gumbPavza);

        paneIgra.getChildren().add(zgornjaMeja);
        paneIgra.getChildren().add(ploscek);
        paneIgra.getChildren().add(zoga);
        paneIgra.getChildren().add(tockeTextIgra);
        paneIgra.getChildren().add(rekordTextIgra);
        paneIgra.getChildren().add(novRekordText);

        paneIgra.setOnMouseMoved(new EventHandler<MouseEvent>() 
        {

        @Override
        public void handle(MouseEvent event) //zazna premikanje miske in s tem premika igralca
        {
            xi=event.getScreenX();
            ploscek.setX(xi);
            if (xi<0) //de ne gre ploscek cez levi rob
            {
                xi=0;
                ploscek.setX(xi);
            }
            if (xi>w-w/15) //de ne gre ploscek cez desni rob
            {
                xi=w-w/15;
                ploscek.setX(xi);
            }

            // yi=event.getScreenY();
        }
        });

        return Igra;
    }

    public void shraniRekord() throws IOException
    {
        File file=new File("rekord.txt");
        try (PrintWriter pw = new PrintWriter(file))
        {
            pw.print(rekord);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public void shraniTocke() throws IOException
    {
        File file=new File("tocke.txt");
        try (PrintWriter pw = new PrintWriter(file))
        {
            pw.print(tocke);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public void preberiRekord() throws IOException
    {
        File file=new File("rekord.txt");
        try (Scanner scanner = new Scanner(file)) 
        {
            if (scanner.hasNextInt()) 
            {
                rekord = scanner.nextInt();
                rekordTextIgra.setText("REKORD: "+rekord);
                rekordTextMeni.setText("REKORD: "+rekord);
            }
        }
        
        catch (IOException e) 
        {
            e.printStackTrace();
        }        
    }

    public void preberiTocke() throws IOException
    {
        File file=new File("tocke.txt");
        try (Scanner scanner = new Scanner(file)) 
        {
            if (scanner.hasNextInt()) 
            {
                tocke = scanner.nextInt();
                tockeTextIgra.setText("TOČKE: "+tocke);
                tockeTextMeni.setText("TOČKE: "+tocke);
            }
        }
        
        catch (IOException e) 
        {
            e.printStackTrace();
        }        
    }

    public void spremeniOkna(Scene scene)
    {
        stage.setScene(scene);
        stage.setFullScreen(true);
    }

    public static void main(String[] args)
    {
        Application.launch(args);
    }

}
