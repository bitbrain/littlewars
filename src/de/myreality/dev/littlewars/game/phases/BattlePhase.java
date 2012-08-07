package de.myreality.dev.littlewars.game.phases;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.helpers.ContextMenuHelper;
import de.myreality.dev.littlewars.components.helpers.ContextMenuHelper.ContextMenu;
import de.myreality.dev.littlewars.components.helpers.ContextMenuHelper.ContextMenuEvent;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.ki.Player;
import de.myreality.dev.littlewars.objects.ArmyUnit;

public class BattlePhase extends BasicGamePhase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BattlePhase(IngameState game) {
		super(game, "Kampf");
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		final Player currentPlayer = game.getCurrentPlayer();		
	
		if (!currentPlayer.hasAvailableUnits() && !ArmyUnit.isUnitBusy()) {
			nextPlayerTurn(currentPlayer, gc);	
		} else {
			if (currentPlayer.isCPU()) {
				currentPlayer.doBattle(delta);
				game.getTopMenu().getBtnPhaseQuit().setEnabled(false);	
			} else if (!ArmyUnit.isUnitBusy()) {
				// Client Player
				game.getTopMenu().getBtnPhaseQuit().setEnabled(true);	
				if (game.getTopMenu().getBtnPhaseQuit().onMouseClick()) {
					// Show a warning, when there exist available units
					if (currentPlayer.hasAvailableUnits()) {
						ContextMenuHelper.getInstance().show(gc, ResourceManager.getInstance().getText("TXT_GAME_WARNING"), 
							     ResourceManager.getInstance().getText("TXT_INFO_ENDTURN"), new ContextMenuEvent() {
								@Override
								public void onAbort(GameContainer gc, StateBasedGame sbg,
										int delta) {
									
								}
								
								@Override
								public void onAccept(GameContainer gc, StateBasedGame sbg,
										int delta) {
									nextPlayerTurn(currentPlayer, gc);
								}				
						});
						ContextMenu menu = ContextMenuHelper.getInstance().getContextMenu();
						menu.setAcceptTextID("TXT_GAME_ENDTURN");
					} else {
						nextPlayerTurn(currentPlayer, gc);		
					}
				}
			} else {
				game.getTopMenu().getBtnPhaseQuit().setEnabled(false);	
			}
		}
	}

}
