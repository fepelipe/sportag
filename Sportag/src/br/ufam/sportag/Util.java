
package br.ufam.sportag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import br.ufam.sportag.model.User;

public class Util
{
	
	// Resposta JSON fake para requisi��o HTTP de amigos.
	// public String jsonString =
	// "{\"meta\":{\"code\":200},\"notifications\":[{\"type\":\"notificationTray\",\"item\":{\"unreadCount\":0}}],\"response\":{\"friends\":{\"count\":42,\"items\":[{\"id\":\"25186752\",\"firstName\":\"Renato\",\"lastName\":\"Vieira\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs1.4sqi.net/img/user/\",\"suffix\":\"/XUMIYBC0545FOLJJ.jpg\"},\"tips\":{\"count\":6},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Manaus, Amazonas\",\"bio\":\"\",\"contact\":{\"facebook\":\"100004322652733\"}},{\"id\":\"30900754\",\"firstName\":\"Claudia\",\"lastName\":\"Rejane\",\"gender\":\"female\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs2.4sqi.net/img/user/\",\"suffix\":\"/QRHFQVCSCHZM513R.jpg\"},\"tips\":{\"count\":4},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"crejane10@gmail.com\",\"twitter\":\"claudiarejane_\",\"facebook\":\"100002059366327\"}},{\"id\":\"17226538\",\"firstName\":\"Antonio\",\"lastName\":\"Eduardo\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs0.4sqi.net/img/user/\",\"suffix\":\"/ZP2Y3JSOB4O3OEP5.jpg\"},\"tips\":{\"count\":5},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":2,\"items\":[]}]},\"homeCity\":\"Manaus, Brasil\",\"bio\":\"\",\"contact\":{\"twitter\":\"heyedu_\"}},{\"id\":\"41081634\",\"firstName\":\"Hector\",\"lastName\":\"Benjamin\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs0.4sqi.net/img/user/\",\"suffix\":\"/BDQ4MSIF1LZDBT4V.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":3,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"phone\":\"+9291920740\",\"email\":\"hector_bem@hotmail.com\",\"twitter\":\"hectorbenjamin0\",\"facebook\":\"100000160096742\"}},{\"id\":\"6219334\",\"firstName\":\"Anderson\",\"lastName\":\"Pimentel\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs1.4sqi.net/img/user/\",\"suffix\":\"/D41QJLTVJFMQOE2P.jpg\"},\"tips\":{\"count\":7},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Manaus, Brasil\",\"bio\":\"\",\"contact\":{\"phone\":\"9293531716\",\"email\":\"andbrain@gmail.com\",\"twitter\":\"andbrain\",\"facebook\":\"100000509427112\"}},{\"id\":\"34698670\",\"firstName\":\"Tiago\",\"lastName\":\"Antunes\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs2.4sqi.net/img/user/\",\"suffix\":\"/1NUPPZVPMNT3DTJI.jpg\"},\"tips\":{\"count\":1},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"tiago_antunes4@hotmail.com\",\"facebook\":\"100000031549903\"}},{\"id\":\"28888050\",\"firstName\":\"Diego\",\"lastName\":\"Rodrigues\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs0.4sqi.net/img/user/\",\"suffix\":\"/IE1OJSODMQCKCOIJ.jpg\"},\"tips\":{\"count\":7},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Manaus, AM\",\"bio\":\"Geek | Science | Video-games | Sports | Music | CRFlamego\",\"contact\":{\"phone\":\"9281997982\",\"email\":\"diegorodrigues.computacao@gmail.com\",\"twitter\":\"digao_rodrigues\",\"facebook\":\"100001362137139\"}},{\"id\":\"33530940\",\"firstName\":\"Crisley\",\"lastName\":\"Linhares\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs2.4sqi.net/img/user/\",\"suffix\":\"/4NU4CVRSFLE0BZFC.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"prestes40@gmail.com\",\"twitter\":\"cprestesl\",\"facebook\":\"1546172690\"}},{\"id\":\"35620542\",\"firstName\":\"Raquel\",\"lastName\":\"Noronha\",\"gender\":\"female\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs3.4sqi.net/img/user/\",\"suffix\":\"/1UWCDJTQZJX23D4L.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"quequel_carioca@hotmail.com\",\"facebook\":\"100002558986906\"}},{\"id\":\"67611446\",\"firstName\":\"Wendel\",\"lastName\":\"Ferraz\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs3.4sqi.net/img/user/\",\"suffix\":\"/RT2Z4GE5RRI423F3.jpg\"},\"tips\":{\"count\":1},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Manaus, AM\",\"bio\":\"20 anos de metamorfose ambulante.\",\"contact\":{\"email\":\"ferrazwm@gmail.com\",\"twitter\":\"possoserindie\",\"facebook\":\"100003355323910\"}},{\"id\":\"41680866\",\"firstName\":\"F�bio\",\"lastName\":\"Lima\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs1.4sqi.net/img/user/\",\"suffix\":\"/MW1YB0UXYFMHKNMS.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Manaus, AM\",\"bio\":\"Graduando em Ci�ncia da Computa��o e Pipoqueiro\",\"contact\":{\"phone\":\"+9291752760\",\"email\":\"lr.fabioml@gmail.com\",\"twitter\":\"fabiooml\",\"facebook\":\"100000667825598\"}},{\"id\":\"27205082\",\"firstName\":\"S�rgio\",\"lastName\":\"Ferreira\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs1.4sqi.net/img/user/\",\"suffix\":\"/4X0UIWKKBKTOEQBU.jpg\"},\"tips\":{\"count\":11},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":2,\"items\":[]}]},\"homeCity\":\"Manaus, Amazonas\",\"bio\":\"Aficcionado em inform�tica, m�sica, bons lugares e uma boa conversa.\",\"contact\":{\"phone\":\"+19294829929\",\"email\":\"sergio_tech@hotmail.com\",\"facebook\":\"100002211048936\"}},{\"id\":\"37802143\",\"firstName\":\"Wellington\",\"lastName\":\"Holanda\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs0.4sqi.net/img/user/\",\"suffix\":\"/50SZUDN0KOYPF1EU.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":2,\"items\":[]}]},\"homeCity\":\"Manaus, Amazonas\",\"bio\":\"Workaholic, Investidor da Bolsa, estudante de Ci�ncia da Computa��o e F� do Roger Federer.\",\"contact\":{\"phone\":\"9281746782\",\"email\":\"wellington_holanda@hotmail.com\",\"twitter\":\"wholanda2\",\"facebook\":\"1522951740\"}},{\"id\":\"35216840\",\"firstName\":\"Rackel\",\"lastName\":\"Hernane\",\"gender\":\"female\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs1.4sqi.net/img/user/\",\"suffix\":\"/LB03EFV3DA4JA3HB.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"rackelhernane@hotmail.com\",\"facebook\":\"100001104278981\"}},{\"id\":\"42037134\",\"firstName\":\"Diego\",\"lastName\":\"Rocha\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs1.4sqi.net/img/user/\",\"suffix\":\"/SEOYY4NDKXZTZUDE.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"diego_dinho_@hotmail.com\",\"twitter\":\"diegorochamdr\",\"facebook\":\"100002078654150\"}},{\"id\":\"13562696\",\"firstName\":\"Raylson\",\"lastName\":\"Brand�o\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs1.4sqi.net/img/user/\",\"suffix\":\"/J3JSJ0LF1U0EYO3N.jpg\"},\"tips\":{\"count\":5},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Manaus, Brasil\",\"bio\":\"Para onde quero chegar? Eis a quest�o.\",\"contact\":{\"email\":\"raylson_gama@hotmail.com\",\"twitter\":\"raylsongb\",\"facebook\":\"1587353847\"}},{\"id\":\"33513540\",\"firstName\":\"Dino\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs3.4sqi.net/img/user/\",\"suffix\":\"/TSFOAGQEEECD41A1.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"phone\":\"+19292167440\",\"email\":\"dino__@rock.com\",\"twitter\":\"dinomarinho\",\"facebook\":\"100002397339066\"}},{\"id\":\"27711193\",\"firstName\":\"Guto\",\"lastName\":\"Kawakami de Oliveira\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs2.4sqi.net/img/user/\",\"suffix\":\"/NUM0A0JXHHT24MHQ.jpg\"},\"tips\":{\"count\":2},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Manaus, Amazonas\",\"bio\":\"\",\"contact\":{\"phone\":\"9291418651\",\"email\":\"kawakami1231234@hotmail.com\",\"twitter\":\"gutokawakami\",\"facebook\":\"100001906478829\"}},{\"id\":\"17413847\",\"firstName\":\"Camila\",\"lastName\":\"Paix�o\",\"gender\":\"female\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs3.4sqi.net/img/user/\",\"suffix\":\"/12NHUDTOZPT4LGTV.jpg\"},\"tips\":{\"count\":2},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Manaus, Brasil\",\"bio\":\"\",\"contact\":{\"email\":\"camilac.paixao@yahoo.com.br\",\"twitter\":\"miilapaixao\",\"facebook\":\"1325379556\"}},{\"id\":\"12154860\",\"firstName\":\"Rafael\",\"lastName\":\"Rodrigues\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs2.4sqi.net/img/user/\",\"suffix\":\"/12154860-WLXCO2OLSOBBWBJL.jpg\"},\"tips\":{\"count\":7},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Manaus\",\"bio\":\"\",\"contact\":{\"phone\":\"9282806932\",\"email\":\"ieurafael@hotmail.com\",\"twitter\":\"rafaeurodrigues\",\"facebook\":\"100001299228215\"}},{\"id\":\"37072647\",\"firstName\":\"Cassia\",\"lastName\":\"Noronha\",\"gender\":\"female\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs0.4sqi.net/img/user/\",\"suffix\":\"/L2WIJCT4CNRZ0FDG.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"cassiaanoronha@hotmail.com\",\"facebook\":\"100002130130936\"}},{\"id\":\"32850671\",\"firstName\":\"Andrielly\",\"lastName\":\"Moura\",\"gender\":\"female\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs2.4sqi.net/img/user/\",\"suffix\":\"/VYOPWVCJ4DGYDUCE.jpg\"},\"tips\":{\"count\":1},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":5,\"items\":[]}]},\"homeCity\":\"Manaus, Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"andriielly.moura@hotmail.com\",\"twitter\":\"andriiellymoura\",\"facebook\":\"100002099624465\"}},{\"id\":\"12290994\",\"firstName\":\"Carlos\",\"lastName\":\"Freitas\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs2.4sqi.net/img/user/\",\"suffix\":\"/YPOKGBQRK5KFE2YK.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Manaus, Brasil\",\"bio\":\"\",\"contact\":{\"email\":\"carlosfs362009@hotmail.com\",\"facebook\":\"100001285488427\"}},{\"id\":\"23318383\",\"firstName\":\"Rodrigo\",\"lastName\":\"Coimbra\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs1.4sqi.net/img/user/\",\"suffix\":\"/XINFRE2YITEEYJVW.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"phone\":\"+19294410608\",\"email\":\"rodrigocoimbraxp@hotmail.com\",\"facebook\":\"100001500069068\"}},{\"id\":\"25937536\",\"firstName\":\"Vanessa\",\"lastName\":\"Azevedo\",\"gender\":\"female\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs1.4sqi.net/img/user/\",\"suffix\":\"/JNDRDSEW5OVP1FK1.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"sp2_lorinhabela@hotmail.com\",\"facebook\":\"100002826166312\"}},{\"id\":\"35200049\",\"firstName\":\"larissa\",\"lastName\":\"noronha\",\"gender\":\"female\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs2.4sqi.net/img/user/\",\"suffix\":\"/E0DZW3CL552HL43B.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Manaus\",\"bio\":\"\",\"contact\":{\"email\":\"noronha.larissa@hotmail.com\",\"facebook\":\"100002079227491\"}},{\"id\":\"42827671\",\"firstName\":\"Rainna\",\"lastName\":\"Lira\",\"gender\":\"female\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs3.4sqi.net/img/user/\",\"suffix\":\"/42827671-DTBX5GHDIM2JYDZC.jpg\"},\"tips\":{\"count\":1},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":2,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"Corinthians s2\",\"contact\":{\"phone\":\"+19293109685\",\"email\":\"rainnaliira@hotmail.com\",\"twitter\":\"rainnalira\",\"facebook\":\"100002086998883\"}},{\"id\":\"38201874\",\"firstName\":\"Daniel\",\"lastName\":\"Aquino\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs2.4sqi.net/img/user/\",\"suffix\":\"/5O3DICJFHNXYLKVV.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Manaus,AM\",\"bio\":\"\",\"contact\":{\"phone\":\"9281580539\",\"email\":\"dhbaquino@gmail.com\",\"facebook\":\"100000375045184\"}},{\"id\":\"16907810\",\"firstName\":\"Rafael\",\"lastName\":\"Yoshida\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs1.4sqi.net/img/user/\",\"suffix\":\"/2ECYKMSFR0XMKK0W.jpg\"},\"tips\":{\"count\":4},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"New York, NY\",\"bio\":\"\",\"contact\":{\"email\":\"yrapha@gmail.com\",\"facebook\":\"100000590302245\"}},{\"id\":\"36580532\",\"firstName\":\"Patita\",\"lastName\":\"Noronha\",\"gender\":\"female\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs1.4sqi.net/img/user/\",\"suffix\":\"/3YIRQ2LLQHRNZ4ET.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"patita_noronha_17@hotmail.com\",\"facebook\":\"100002010863013\"}},{\"id\":\"29229670\",\"firstName\":\"Diana\",\"lastName\":\"Lemos\",\"gender\":\"female\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs1.4sqi.net/img/user/\",\"suffix\":\"/JI2EZACT1SASA2DC.jpg\"},\"tips\":{\"count\":1},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":2,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"ds.lemos@hotmail.com\",\"twitter\":\"diannalemos\",\"facebook\":\"100001991236842\"}},{\"id\":\"41352042\",\"firstName\":\"Castro\",\"lastName\":\"JulianaAzevedo\",\"gender\":\"female\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs0.4sqi.net/img/user/\",\"suffix\":\"/YQB1NRIRWVM0Q5GZ.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"julianna_azevedo@hotmail.com\",\"facebook\":\"100002230917497\"}},{\"id\":\"20210629\",\"firstName\":\"Heloisa\",\"lastName\":\"Souza\",\"gender\":\"female\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs0.4sqi.net/img/user/\",\"suffix\":\"/DYQSDUPWW0HX4JMT.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Cear�\",\"bio\":\"\",\"contact\":{\"email\":\"heloca_nascimento@hotmail.com\",\"facebook\":\"100002158397400\"}},{\"id\":\"28203526\",\"firstName\":\"Dino\",\"lastName\":\"Marinho\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs3.4sqi.net/img/user/\",\"suffix\":\"/325MQO50IA0SNYG1.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"dino__@rock.con\"}},{\"id\":\"281012\",\"firstName\":\"Awdren\",\"lastName\":\"Font�o\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs2.4sqi.net/img/user/\",\"suffix\":\"/5GZCJTJPHVQUTNWW.jpg\"},\"tips\":{\"count\":22},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Manaus, Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"awdrenfontao@gmail.com\",\"twitter\":\"awdren\",\"facebook\":\"100000428091172\"}},{\"id\":\"6400003\",\"firstName\":\"Phillip\",\"lastName\":\"Furtado\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs1.4sqi.net/img/user/\",\"suffix\":\"/K0524DQ1VKUVFJK5.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":3,\"items\":[]}]},\"homeCity\":\"\",\"bio\":\"\",\"contact\":{\"facebook\":\"501352680\"}},{\"id\":\"32249506\",\"firstName\":\"Claudia\",\"lastName\":\"Amaral\",\"gender\":\"female\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs2.4sqi.net/img/user/\",\"suffix\":\"/OTT3UY55EMS0II1W.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"claudinha_am@yahoo.com.br\",\"facebook\":\"100001684315936\"}},{\"id\":\"41655271\",\"firstName\":\"Neto\",\"lastName\":\"Costa\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs1.4sqi.net/img/user/\",\"suffix\":\"/1L10KIVKRRBB4LE2.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"neto_costa00@hotmail.com\",\"facebook\":\"100002433103231\"}},{\"id\":\"23138425\",\"firstName\":\"lucas leonny\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs1.4sqi.net/img/user/\",\"suffix\":\"/Q4JSRHUER5IS5HMK.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"brasil\",\"bio\":\"\",\"contact\":{\"phone\":\"9581070595\",\"email\":\"leonny_94@hotmail.com\",\"twitter\":\"lucasleonny\",\"facebook\":\"100000157048023\"}},{\"id\":\"9097588\",\"firstName\":\"Javier\",\"lastName\":\"Ferreira\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs0.4sqi.net/img/user/\",\"suffix\":\"/KXXXYWKZOGPG1QP0.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Manaus, Brasil\",\"bio\":\"\",\"contact\":{\"phone\":\"9281270252\",\"email\":\"zambrano.ferreira@gmail.com\",\"twitter\":\"jzferreira\",\"facebook\":\"100001222160671\"}},{\"id\":\"34169036\",\"firstName\":\"Caroline\",\"lastName\":\"Souza\",\"gender\":\"female\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs2.4sqi.net/img/user/\",\"suffix\":\"/2NVWTL2PR2W5F1M4.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Amazonas\",\"bio\":\"\",\"contact\":{\"email\":\"caroolinesouza1@gmail.com\",\"twitter\":\"carolinesoouza_\",\"facebook\":\"100002055286346\"}},{\"id\":\"16912355\",\"firstName\":\"Renato\",\"lastName\":\"Lima\",\"gender\":\"male\",\"relationship\":\"friend\",\"photo\":{\"prefix\":\"https://irs0.4sqi.net/img/user/\",\"suffix\":\"/5BUEEWZY4P4B3YXS.jpg\"},\"tips\":{\"count\":0},\"lists\":{\"groups\":[{\"type\":\"created\",\"count\":1,\"items\":[]}]},\"homeCity\":\"Manaus, Brasil\",\"bio\":\"\",\"contact\":{\"email\":\"renatoaugustoml@gmail.com\",\"twitter\":\"renatofu_\",\"facebook\":\"100001327563499\"}}]},\"checksum\":\"9aae01c9b2abf590e73826b3d8980758276ee996\"}}";
	
	public static String sportagServerUrl = "http://sportag.net76.net/";
	public static String selfDataRequestUrl = "https://api.foursquare.com/v2/users/self";
	public static String addUrl = "http://sportag.net76.net/add.php";
	
	public JSONObject jsonParser(String dataString) throws JSONException
	{
		JSONObject root = new JSONObject(dataString);
		return root;
	}
	
	// M�todo para obten��o de informa��es sobre um User
	public User getUser(String jsonString) throws JSONException
	{
		JSONObject root = jsonParser(jsonString);
		
		JSONObject response = root.getJSONObject("response");
		JSONObject userObject = response.getJSONObject("user");
		
		User user = createUser(userObject);
		
		return user;
	}
	
	public User createUser(JSONObject jsonUser) throws JSONException
	{
		
		JSONObject photo = jsonUser.getJSONObject("photo");
		
		Integer id = jsonUser.optInt("id");
		String firstName = jsonUser.optString("firstName");
		String lastName = jsonUser.optString("lastName");
		String gender = jsonUser.optString("gender");
		String relationship = jsonUser.optString("relationship");
		String homeCity = jsonUser.optString("homeCity");
		String bio = jsonUser.optString("bio");
		String photoURL = photo.optString("prefix") + "36x36" + photo.optString("suffix");
		
		User newUser = new User();
		
		newUser.id = id;
		newUser.firstName = firstName;
		newUser.lastName = lastName;
		newUser.gender = gender;
		newUser.relationship = relationship;
		newUser.homeCity = homeCity;
		newUser.bio = bio;
		newUser.photoPrefix = photo.optString("prefix");
		newUser.photoSuffix = photo.optString("suffix");
		
		return newUser;
	}
	
	// M�todo para obten��o dos amigos sob o JSON em jsonString
	public ArrayList<User> getFriends(String jsonString) throws JSONException
	{
		
		ArrayList<User> responseFriends = new ArrayList<User>();
		
		JSONObject root = jsonParser(jsonString);
		
		JSONObject response = root.getJSONObject("response");
		JSONObject friends = response.getJSONObject("friends");
		JSONArray items = friends.getJSONArray("items");
		
		for (int i = 0; i < items.length(); i++)
		{
			JSONObject friendObject = items.getJSONObject(i);
			if (friendObject.optString("type").isEmpty())
			{
				
				User user = createUser(friendObject);
				responseFriends.add(user);
			}
		}
		
		return responseFriends;
	}
	
	public static String streamToString(final InputStream inputStream) throws IOException
	{
		String stringAux = "";
		
		if (inputStream != null)
		{
			final StringBuilder stbuilder = new StringBuilder();
			String line;
			
			try
			{
				final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				
				while ((line = reader.readLine()) != null)
				{
					stbuilder.append(line);
				}
				
				reader.close();
			} finally
			{
				inputStream.close();
			}
			
			stringAux = stbuilder.toString();
		}
		
		return stringAux;
	}
	
	public static String dictionaryToString(HashMap<String, Object> args)
	{
		return dictionaryToString(args, true);
	}
	
	public static String dictionaryToString(HashMap<String, Object> args,
			boolean needEncode)
	{
		String argsString = "";
		
		if (args != null)
		{
			argsString += "?";
			for (Entry<String, Object> item : args.entrySet())
			{
				String value = "";
				if (item.getValue() != null)
				{
					value = item.getValue().toString();
					
					if (needEncode)
						value = Uri.encode(value);
				}
				argsString += String.format("%s=%s&", item.getKey(), value);
			}
			argsString = argsString.substring(0, argsString.length() - 1);
		}
		
		return argsString;
	}
	
	public static Bitmap loadBitmap(String url)
	{
		URL newurl;
		Bitmap bitmap = null;
		try
		{
			newurl = new URL(url);
			bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
			
		} catch (MalformedURLException e)
		{
			Log.e("Erro", "MalFormedURL", e);
		} catch (IOException e)
		{
			Log.e("Erro", "IO", e);
		}
		
		return bitmap;
	}
}
