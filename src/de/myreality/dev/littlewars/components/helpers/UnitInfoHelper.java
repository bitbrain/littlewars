/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://littlewars.my-reality.de
 * 
 * Helper for displaying information above an unit
 * 
 * @version 	0.5.18
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.components.helpers;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.components.FadeInfoSetting;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.gui.FadeInfo;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.objects.CommandoCenter;
import de.myreality.dev.littlewars.objects.GameObject;

public class UnitInfoHelper {

	private static UnitInfoHelper _instance;
	
	// All current unit informations
	List<FadeInfo> infos;
	
	static {
		_instance = new UnitInfoHelper();
	}
	
	public UnitInfoHelper() {
		infos = new ArrayList<FadeInfo>();
	}
	
	public static UnitInfoHelper getInstance() {
		return _instance;
	}
	
	public FadeInfo addInfo(GameObject target, FadeInfoSetting setting, GameContainer gc, IngameState game) {
		FadeInfo info = null;
		if (!infos.isEmpty()) {
			FadeInfo last = infos.get(infos.size() - 1);
			if (infos.get(0).getOpacity() > 0.1) {
				info = new FadeInfo(target, setting, gc, last, game);
			} else {				
				info = new FadeInfo(target, setting, gc, null, game);
			}
		} else {
			info = new FadeInfo(target, setting, gc, null, game);
		}
		 
		infos.add(info);
		return info;
	}
	
	public void removeInfo(FadeInfo info) {
		infos.remove(info);
	}
	
	public void render(Graphics g) {
		for (FadeInfo info: infos) {
			info.draw(g);
		}
	}
	
	public void update(int delta) {
		boolean containsOnlyCenterInfo = true;
		for (FadeInfo info: infos) {
			info.update(delta);
			if (!(info.getTarget() instanceof CommandoCenter)) {
				containsOnlyCenterInfo = false;
			}
			if (info.isDone()) {
				info.clear();
				removeInfo(info);
				break;
			}
		}
		if (!infos.isEmpty() && !containsOnlyCenterInfo) {
			ArmyUnit.setUnitBusy(true);
		}
	}
}
