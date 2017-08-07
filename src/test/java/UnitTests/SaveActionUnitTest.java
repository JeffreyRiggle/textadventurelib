package UnitTests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import playerlib.equipment.Equipment;
import playerlib.inventory.Inventory;
import playerlib.player.IPlayer;
import playerlib.player.Player;
import textadventurelib.actions.IAction;
import textadventurelib.core.ExecutionParameters;
import textadventurelib.persistence.SaveAction;

public class SaveActionUnitTest {

	private final String SAVE_LOCATION = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/SaveData.xml";
	private final String GAME_STATES_LOC = System.getProperty("user.home") + "/ilusr/UnitTests/TAL/SaveGameStatesData.xml";
	
	@Test
	public void testCreate() {
		try {
			IAction save = new SaveAction(SAVE_LOCATION, true, GAME_STATES_LOC);
			
			assertNotNull(save);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testExecution() {
		try {
			File saveFile = new File(SAVE_LOCATION);
			
			if (saveFile.exists()) {
				saveFile.delete();
			}
			
			IAction save = new SaveAction(SAVE_LOCATION, true, GAME_STATES_LOC);
			
			List<IPlayer> players = new ArrayList<IPlayer>();
			IPlayer playerA = new Player("Deemo");
			playerA.inventory(new Inventory());
			playerA.equipment(new Equipment());
			players.add(playerA);
			
			ExecutionParameters params = new ExecutionParameters(players, "aa");
			save.execute(params);
			assertTrue(new File(SAVE_LOCATION).exists());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testRelativeExecution() {
		try {
			String savePath = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent() + "/save.xml";
			File saveFile = new File(savePath);
			
			if (saveFile.exists()) {
				saveFile.delete();
			}
			
			IAction save = new SaveAction("./save.xml", true, GAME_STATES_LOC);
			
			List<IPlayer> players = new ArrayList<IPlayer>();
			IPlayer playerA = new Player("Deemo");
			playerA.inventory(new Inventory());
			playerA.equipment(new Equipment());
			players.add(playerA);
			
			ExecutionParameters params = new ExecutionParameters(players, "aa");
			save.execute(params);
			assertTrue(new File(savePath).exists());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
}
