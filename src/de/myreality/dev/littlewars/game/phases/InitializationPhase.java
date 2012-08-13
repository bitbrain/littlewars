package de.myreality.dev.littlewars.game.phases;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.helpers.ContextMenuHelper;
import de.myreality.dev.littlewars.components.helpers.ContextMenuHelper.ContextMenu;
import de.myreality.dev.littlewars.components.helpers.FlashHelper;
import de.myreality.dev.littlewars.components.helpers.ContextMenuHelper.ContextMenuEvent;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.gui.PhaseInfo;
import de.myreality.dev.littlewars.ki.Player;
import de.myreality.dev.littlewars.objects.ArmyUnit;

public class InitializationPhase extends BasicGamePhase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InitializationPhase(IngameState game) {
		super(game, "Initialisierung");

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		final Player currentPlayer = game.getCurrentPlayer();
		if (ArmyUnit.isUnitBusy()) {			
			game.setPhase(IngameState.BATTLE);
			String message = "";
			if (game.getCurrentPlayer().isClientPlayer()) {
				message = "Die Schlacht beginnt!";
			}
			FlashHelper.getInstance().flash(new PhaseInfo(currentPlayer, game.getPhase(), message, gc), 1200, gc);
		} else {		
			if (currentPlayer.isCPU()) {
				game.getTopMenu().getBtnPhaseQuit().setEnabled(false);		
				
				// End the turn when nothing to do
				if (!currentPlayer.doInitialisation(delta) && !ArmyUnit.isUnitBusy()) {
					nextPlayerTurn(currentPlayer, gc);			
				}
			} else if (!ArmyUnit.isUnitBusy()) {
				game.getTopMenu().getBtnPhaseQuit().setEnabled(true);
				// Client Player
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
