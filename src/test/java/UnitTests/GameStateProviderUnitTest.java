package UnitTests;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import ilusr.gamestatemanager.IGameState;
import textadventurelib.gamestates.GameStateProvider;
import textadventurelib.persistence.CompletionActionPersistence;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.LayoutInfoPersistenceObject;
import textadventurelib.persistence.LayoutType;
import textadventurelib.persistence.MatchType;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.TextAdventurePersistenceObject;
import textadventurelib.persistence.TextTriggerPersistenceObject;

public class GameStateProviderUnitTest {

	@Test
	public void testCreate() {
		try {
			TextAdventurePersistenceObject source = new TextAdventurePersistenceObject();
			GameStateProvider provider = new GameStateProvider(source);
			assertNotNull(provider);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void testLinearProvide() {
		try {
			TextAdventurePersistenceObject source = new TextAdventurePersistenceObject();
			
			LayoutInfoPersistenceObject layout = new LayoutInfoPersistenceObject();
			layout.setLayoutType(LayoutType.TextWithTextInput);
			
			GameStatePersistenceObject gs1 = new GameStatePersistenceObject("gs1");
			gs1.layout(layout);
			TextTriggerPersistenceObject trg1 = new TextTriggerPersistenceObject();
			trg1.caseSensitive(false);
			trg1.matchType(MatchType.Contains);
			trg1.text("next");
			CompletionActionPersistence comp1 = new CompletionActionPersistence();
			comp1.completionData("gs2");
			OptionPersistenceObject opt1 = new OptionPersistenceObject();
			opt1.action(comp1);
			opt1.triggers().add(trg1);
			gs1.addOption(opt1);
			
			GameStatePersistenceObject gs2 = new GameStatePersistenceObject("gs2");
			gs2.layout(layout);
			TextTriggerPersistenceObject trg2 = new TextTriggerPersistenceObject();
			trg2.caseSensitive(false);
			trg2.matchType(MatchType.Contains);
			trg2.text("next");
			CompletionActionPersistence comp2 = new CompletionActionPersistence();
			comp2.completionData("gs3");
			OptionPersistenceObject opt2 = new OptionPersistenceObject();
			opt2.action(comp2);
			opt2.triggers().add(trg2);
			gs2.addOption(opt2);
			
			GameStatePersistenceObject gs3 = new GameStatePersistenceObject("gs3");
			gs3.layout(layout);
			TextTriggerPersistenceObject trg3 = new TextTriggerPersistenceObject();
			trg3.caseSensitive(false);
			trg3.matchType(MatchType.Contains);
			trg3.text("next");
			CompletionActionPersistence comp3 = new CompletionActionPersistence();
			comp3.completionData("gs4");
			OptionPersistenceObject opt3 = new OptionPersistenceObject();
			opt3.action(comp3);
			opt3.triggers().add(trg3);
			gs3.addOption(opt3);
			
			GameStatePersistenceObject gs4 = new GameStatePersistenceObject("gs4");
			gs4.layout(layout);
			TextTriggerPersistenceObject trg4 = new TextTriggerPersistenceObject();
			trg4.caseSensitive(false);
			trg4.matchType(MatchType.Contains);
			trg4.text("next");
			CompletionActionPersistence comp4 = new CompletionActionPersistence();
			comp4.completionData("gs5");
			OptionPersistenceObject opt4 = new OptionPersistenceObject();
			opt4.action(comp4);
			opt4.triggers().add(trg4);
			gs4.addOption(opt4);
			
			GameStatePersistenceObject gs5 = new GameStatePersistenceObject("gs5");
			gs5.layout(layout);
			TextTriggerPersistenceObject trg5 = new TextTriggerPersistenceObject();
			trg5.caseSensitive(false);
			trg5.matchType(MatchType.Contains);
			trg5.text("next");
			CompletionActionPersistence comp5 = new CompletionActionPersistence();
			comp5.completionData("gs6");
			OptionPersistenceObject opt5 = new OptionPersistenceObject();
			opt5.action(comp5);
			opt5.triggers().add(trg5);
			gs5.addOption(opt5);
			
			GameStatePersistenceObject gs6 = new GameStatePersistenceObject("gs6");
			gs6.layout(layout);
			TextTriggerPersistenceObject trg6 = new TextTriggerPersistenceObject();
			trg6.caseSensitive(false);
			trg6.matchType(MatchType.Contains);
			trg6.text("next");
			CompletionActionPersistence comp6 = new CompletionActionPersistence();
			comp6.completionData("gs7");
			OptionPersistenceObject opt6 = new OptionPersistenceObject();
			opt6.action(comp6);
			opt6.triggers().add(trg6);
			gs6.addOption(opt6);
			
			GameStatePersistenceObject gs7 = new GameStatePersistenceObject("gs7");
			gs7.layout(layout);
			TextTriggerPersistenceObject trg7 = new TextTriggerPersistenceObject();
			trg7.caseSensitive(false);
			trg7.matchType(MatchType.Contains);
			trg7.text("next");
			CompletionActionPersistence comp7 = new CompletionActionPersistence();
			comp7.completionData("gs1");
			OptionPersistenceObject opt7 = new OptionPersistenceObject();
			opt7.action(comp7);
			opt7.triggers().add(trg7);
			gs7.addOption(opt7);
			
			source.addGameState(gs1);
			source.addGameState(gs2);
			source.addGameState(gs3);
			source.addGameState(gs4);
			source.addGameState(gs5);
			source.addGameState(gs6);
			source.addGameState(gs7);
			
			source.gameStatesInline(true);
			source.prepareXml();
			GameStateProvider provider = new GameStateProvider(source);
			Map<Object, IGameState> buffer = provider.provideGameStates(5, "gs2");
			assertEquals(5, buffer.size());
			assertTrue(buffer.containsKey(gs2.stateId()));
			assertTrue(buffer.containsKey(gs3.stateId()));
			assertTrue(buffer.containsKey(gs4.stateId()));
			assertTrue(buffer.containsKey(gs5.stateId()));
			assertTrue(buffer.containsKey(gs6.stateId()));
			assertNotNull(provider);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testLinearProvideWithBufferOfTwo() {
		try {
			TextAdventurePersistenceObject source = new TextAdventurePersistenceObject();
			
			LayoutInfoPersistenceObject layout = new LayoutInfoPersistenceObject();
			layout.setLayoutType(LayoutType.TextWithTextInput);
			
			GameStatePersistenceObject gs1 = new GameStatePersistenceObject("gs1");
			gs1.layout(layout);
			TextTriggerPersistenceObject trg1 = new TextTriggerPersistenceObject();
			trg1.caseSensitive(false);
			trg1.matchType(MatchType.Contains);
			trg1.text("next");
			CompletionActionPersistence comp1 = new CompletionActionPersistence();
			comp1.completionData("gs2");
			OptionPersistenceObject opt1 = new OptionPersistenceObject();
			opt1.action(comp1);
			opt1.triggers().add(trg1);
			gs1.addOption(opt1);
			
			GameStatePersistenceObject gs2 = new GameStatePersistenceObject("gs2");
			gs2.layout(layout);
			TextTriggerPersistenceObject trg2 = new TextTriggerPersistenceObject();
			trg2.caseSensitive(false);
			trg2.matchType(MatchType.Contains);
			trg2.text("next");
			CompletionActionPersistence comp2 = new CompletionActionPersistence();
			comp2.completionData("gs3");
			OptionPersistenceObject opt2 = new OptionPersistenceObject();
			opt2.action(comp2);
			opt2.triggers().add(trg2);
			gs2.addOption(opt2);
			
			GameStatePersistenceObject gs3 = new GameStatePersistenceObject("gs3");
			gs3.layout(layout);
			TextTriggerPersistenceObject trg3 = new TextTriggerPersistenceObject();
			trg3.caseSensitive(false);
			trg3.matchType(MatchType.Contains);
			trg3.text("next");
			CompletionActionPersistence comp3 = new CompletionActionPersistence();
			comp3.completionData("gs4");
			OptionPersistenceObject opt3 = new OptionPersistenceObject();
			opt3.action(comp3);
			opt3.triggers().add(trg3);
			gs3.addOption(opt3);
			
			GameStatePersistenceObject gs4 = new GameStatePersistenceObject("gs4");
			gs4.layout(layout);
			TextTriggerPersistenceObject trg4 = new TextTriggerPersistenceObject();
			trg4.caseSensitive(false);
			trg4.matchType(MatchType.Contains);
			trg4.text("next");
			CompletionActionPersistence comp4 = new CompletionActionPersistence();
			comp4.completionData("gs5");
			OptionPersistenceObject opt4 = new OptionPersistenceObject();
			opt4.action(comp4);
			opt4.triggers().add(trg4);
			gs4.addOption(opt4);
			
			GameStatePersistenceObject gs5 = new GameStatePersistenceObject("gs5");
			gs5.layout(layout);
			TextTriggerPersistenceObject trg5 = new TextTriggerPersistenceObject();
			trg5.caseSensitive(false);
			trg5.matchType(MatchType.Contains);
			trg5.text("next");
			CompletionActionPersistence comp5 = new CompletionActionPersistence();
			comp5.completionData("gs6");
			OptionPersistenceObject opt5 = new OptionPersistenceObject();
			opt5.action(comp5);
			opt5.triggers().add(trg5);
			gs5.addOption(opt5);
			
			GameStatePersistenceObject gs6 = new GameStatePersistenceObject("gs6");
			gs6.layout(layout);
			TextTriggerPersistenceObject trg6 = new TextTriggerPersistenceObject();
			trg6.caseSensitive(false);
			trg6.matchType(MatchType.Contains);
			trg6.text("next");
			CompletionActionPersistence comp6 = new CompletionActionPersistence();
			comp6.completionData("gs7");
			OptionPersistenceObject opt6 = new OptionPersistenceObject();
			opt6.action(comp6);
			opt6.triggers().add(trg6);
			gs6.addOption(opt6);
			
			GameStatePersistenceObject gs7 = new GameStatePersistenceObject("gs7");
			gs7.layout(layout);
			TextTriggerPersistenceObject trg7 = new TextTriggerPersistenceObject();
			trg7.caseSensitive(false);
			trg7.matchType(MatchType.Contains);
			trg7.text("next");
			CompletionActionPersistence comp7 = new CompletionActionPersistence();
			comp7.completionData("gs1");
			OptionPersistenceObject opt7 = new OptionPersistenceObject();
			opt7.action(comp7);
			opt7.triggers().add(trg7);
			gs7.addOption(opt7);
			
			source.addGameState(gs1);
			source.addGameState(gs2);
			source.addGameState(gs3);
			source.addGameState(gs4);
			source.addGameState(gs5);
			source.addGameState(gs6);
			source.addGameState(gs7);
			
			source.gameStatesInline(true);
			source.prepareXml();
			GameStateProvider provider = new GameStateProvider(source);
			Map<Object, IGameState> buffer = provider.provideGameStates(2, "gs2");
			assertEquals(2, buffer.size());
			assertTrue(buffer.containsKey(gs3.stateId()));
			assertNotNull(provider);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testNestedProvide() {
		try {
			TextAdventurePersistenceObject source = new TextAdventurePersistenceObject();
			
			LayoutInfoPersistenceObject layout = new LayoutInfoPersistenceObject();
			layout.setLayoutType(LayoutType.TextWithTextInput);
			
			GameStatePersistenceObject gs1 = new GameStatePersistenceObject("gs1");
			gs1.layout(layout);
			TextTriggerPersistenceObject trg1 = new TextTriggerPersistenceObject();
			trg1.caseSensitive(false);
			trg1.matchType(MatchType.Contains);
			trg1.text("next");
			CompletionActionPersistence comp1 = new CompletionActionPersistence();
			comp1.completionData("gs2");
			OptionPersistenceObject opt1 = new OptionPersistenceObject();
			opt1.action(comp1);
			opt1.triggers().add(trg1);
			gs1.addOption(opt1);
			TextTriggerPersistenceObject trg1a = new TextTriggerPersistenceObject();
			trg1a.caseSensitive(false);
			trg1a.matchType(MatchType.Contains);
			trg1a.text("nexta");
			CompletionActionPersistence comp1a = new CompletionActionPersistence();
			comp1a.completionData("gs3");
			OptionPersistenceObject opt1a = new OptionPersistenceObject();
			opt1a.action(comp1a);
			opt1a.triggers().add(trg1a);
			gs1.addOption(opt1a);
			
			GameStatePersistenceObject gs2 = new GameStatePersistenceObject("gs2");
			gs2.layout(layout);
			TextTriggerPersistenceObject trg2 = new TextTriggerPersistenceObject();
			trg2.caseSensitive(false);
			trg2.matchType(MatchType.Contains);
			trg2.text("next");
			CompletionActionPersistence comp2 = new CompletionActionPersistence();
			comp2.completionData("gs3");
			OptionPersistenceObject opt2 = new OptionPersistenceObject();
			opt2.action(comp2);
			opt2.triggers().add(trg2);
			gs2.addOption(opt2);
			TextTriggerPersistenceObject trg2a = new TextTriggerPersistenceObject();
			trg2a.caseSensitive(false);
			trg2a.matchType(MatchType.Contains);
			trg2a.text("nexta");
			CompletionActionPersistence comp2a = new CompletionActionPersistence();
			comp2a.completionData("gs4");
			OptionPersistenceObject opt2a = new OptionPersistenceObject();
			opt2a.action(comp2a);
			opt2a.triggers().add(trg2a);
			gs2.addOption(opt2a);
			
			GameStatePersistenceObject gs3 = new GameStatePersistenceObject("gs3");
			gs3.layout(layout);
			TextTriggerPersistenceObject trg3 = new TextTriggerPersistenceObject();
			trg3.caseSensitive(false);
			trg3.matchType(MatchType.Contains);
			trg3.text("next");
			CompletionActionPersistence comp3 = new CompletionActionPersistence();
			comp3.completionData("gs4");
			OptionPersistenceObject opt3 = new OptionPersistenceObject();
			opt3.action(comp3);
			opt3.triggers().add(trg3);
			gs3.addOption(opt3);
			TextTriggerPersistenceObject trg3a = new TextTriggerPersistenceObject();
			trg3a.caseSensitive(false);
			trg3a.matchType(MatchType.Contains);
			trg3a.text("nexta");
			CompletionActionPersistence comp3a = new CompletionActionPersistence();
			comp3a.completionData("gs5");
			OptionPersistenceObject opt3a = new OptionPersistenceObject();
			opt3a.action(comp3a);
			opt3a.triggers().add(trg3a);
			gs3.addOption(opt3a);
			
			GameStatePersistenceObject gs4 = new GameStatePersistenceObject("gs4");
			gs4.layout(layout);
			TextTriggerPersistenceObject trg4 = new TextTriggerPersistenceObject();
			trg4.caseSensitive(false);
			trg4.matchType(MatchType.Contains);
			trg4.text("next");
			CompletionActionPersistence comp4 = new CompletionActionPersistence();
			comp4.completionData("gs5");
			OptionPersistenceObject opt4 = new OptionPersistenceObject();
			opt4.action(comp4);
			opt4.triggers().add(trg4);
			gs4.addOption(opt4);
			TextTriggerPersistenceObject trg4a = new TextTriggerPersistenceObject();
			trg4a.caseSensitive(false);
			trg4a.matchType(MatchType.Contains);
			trg4a.text("nexta");
			CompletionActionPersistence comp4a = new CompletionActionPersistence();
			comp4a.completionData("gs6");
			OptionPersistenceObject opt4a = new OptionPersistenceObject();
			opt4a.action(comp4a);
			opt4a.triggers().add(trg4a);
			gs4.addOption(opt4a);
			
			GameStatePersistenceObject gs5 = new GameStatePersistenceObject("gs5");
			gs5.layout(layout);
			TextTriggerPersistenceObject trg5 = new TextTriggerPersistenceObject();
			trg5.caseSensitive(false);
			trg5.matchType(MatchType.Contains);
			trg5.text("next");
			CompletionActionPersistence comp5 = new CompletionActionPersistence();
			comp5.completionData("gs6");
			OptionPersistenceObject opt5 = new OptionPersistenceObject();
			opt5.action(comp5);
			opt5.triggers().add(trg5);
			gs5.addOption(opt5);
			TextTriggerPersistenceObject trg5a = new TextTriggerPersistenceObject();
			trg5a.caseSensitive(false);
			trg5a.matchType(MatchType.Contains);
			trg5a.text("nexta");
			CompletionActionPersistence comp5a = new CompletionActionPersistence();
			comp5a.completionData("gs7");
			OptionPersistenceObject opt5a = new OptionPersistenceObject();
			opt5a.action(comp5a);
			opt5a.triggers().add(trg5a);
			gs5.addOption(opt5a);
			
			GameStatePersistenceObject gs6 = new GameStatePersistenceObject("gs6");
			gs6.layout(layout);
			TextTriggerPersistenceObject trg6 = new TextTriggerPersistenceObject();
			trg6.caseSensitive(false);
			trg6.matchType(MatchType.Contains);
			trg6.text("next");
			CompletionActionPersistence comp6 = new CompletionActionPersistence();
			comp6.completionData("gs7");
			OptionPersistenceObject opt6 = new OptionPersistenceObject();
			opt6.action(comp6);
			opt6.triggers().add(trg6);
			gs6.addOption(opt6);
			TextTriggerPersistenceObject trg6a = new TextTriggerPersistenceObject();
			trg6a.caseSensitive(false);
			trg6a.matchType(MatchType.Contains);
			trg6a.text("nexta");
			CompletionActionPersistence comp6a = new CompletionActionPersistence();
			comp6a.completionData("gs1");
			OptionPersistenceObject opt6a = new OptionPersistenceObject();
			opt6a.action(comp6a);
			opt6a.triggers().add(trg6a);
			gs6.addOption(opt6a);
			
			GameStatePersistenceObject gs7 = new GameStatePersistenceObject("gs7");
			gs7.layout(layout);
			TextTriggerPersistenceObject trg7 = new TextTriggerPersistenceObject();
			trg7.caseSensitive(false);
			trg7.matchType(MatchType.Contains);
			trg7.text("next");
			CompletionActionPersistence comp7 = new CompletionActionPersistence();
			comp7.completionData("gs1");
			OptionPersistenceObject opt7 = new OptionPersistenceObject();
			opt7.action(comp7);
			opt7.triggers().add(trg7);
			gs7.addOption(opt7);
			TextTriggerPersistenceObject trg7a = new TextTriggerPersistenceObject();
			trg7a.caseSensitive(false);
			trg7a.matchType(MatchType.Contains);
			trg7a.text("nexta");
			CompletionActionPersistence comp7a = new CompletionActionPersistence();
			comp7a.completionData("gs2");
			OptionPersistenceObject opt7a = new OptionPersistenceObject();
			opt7a.action(comp7a);
			opt7a.triggers().add(trg7a);
			gs7.addOption(opt7a);
			
			source.addGameState(gs1);
			source.addGameState(gs2);
			source.addGameState(gs3);
			source.addGameState(gs4);
			source.addGameState(gs5);
			source.addGameState(gs6);
			source.addGameState(gs7);
			
			source.gameStatesInline(true);
			source.prepareXml();
			GameStateProvider provider = new GameStateProvider(source);
			Map<Object, IGameState> buffer = provider.provideGameStates(5, "gs2");
			assertEquals(5, buffer.size());
			assertTrue(buffer.containsKey(gs2.stateId()));
			assertTrue(buffer.containsKey(gs3.stateId()));
			assertTrue(buffer.containsKey(gs4.stateId()));
			assertTrue(buffer.containsKey(gs5.stateId()));
			assertTrue(buffer.containsKey(gs6.stateId()));
			assertNotNull(provider);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
}
