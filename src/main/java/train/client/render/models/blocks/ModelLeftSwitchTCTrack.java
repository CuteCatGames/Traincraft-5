package train.client.render.models.blocks;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import tmt.ModelBase;
import tmt.ModelRendererTurbo;
import tmt.Tessellator;
import train.common.library.Info;
import train.common.tile.TileTCRail;

@SideOnly(Side.CLIENT)
public class ModelLeftSwitchTCTrack extends ModelBase {
	private ModelRendererTurbo modelMediumLeftSwitchActive = new ModelRendererTurbo(this).addObj("track_switch_small_active_left_new.obj");
	private ModelRendererTurbo modelMediumLeftSwitchInactive = new ModelRendererTurbo(this).addObj("track_switch_small_inactive_left_new.obj");
	private ModelRendererTurbo modelMediumLeftParallelSwitchInactive = new ModelRendererTurbo(this).addObj("track_switch_parallel_inactive_left.obj");
	private ModelRendererTurbo modelMediumLeftParallelSwitchActive = new ModelRendererTurbo(this).addObj("track_switch_parallel_active_left.obj");
	private ModelRendererTurbo modelLargeLeftSwitchActive = new ModelRendererTurbo(this).addObj("track_switch_medium_active_left.obj");
	private ModelRendererTurbo modelLargeLeftSwitchInactive = new ModelRendererTurbo(this).addObj("track_switch_medium_inactive_left.obj");


	public ModelLeftSwitchTCTrack() {

	}

	public void renderMediumActive() {
		modelMediumLeftSwitchActive.render();
	}
	public void renderMediumInactive() {
		modelMediumLeftSwitchInactive.render();
	}
	public void renderMediumParallelInactive() {
		modelMediumLeftParallelSwitchInactive.render();
	}
	public void renderMediumParallelActive() {
		modelMediumLeftParallelSwitchActive.render();
	}
	public void renderLarge90Active() {
		modelLargeLeftSwitchActive.render();
	}
	public void renderLarge90Inactive() {
		modelLargeLeftSwitchInactive.render();
	}

	public void render(String type, TileTCRail tcRail, double x, double y, double z) {

		// Bind the texture, so that OpenGL properly textures our block.
		Tessellator.bindTexture(new ResourceLocation(Info.resourceLocation, Info.modelTexPrefix + "track_normal.png"));
		GL11.glColor4f(1, 1, 1, 1);
		//GL11.glScalef(0.5f, 0.5f, 0.5f);

		if (tcRail.getFacing() == 3) {
			if(type.equals("medium")){
				GL11.glTranslatef(-1.0f, 0.0f, 1.0f);
			}
			else if(type.equals("large_90")){
				GL11.glRotatef(0, 0, 1, 0);
				GL11.glTranslatef(0.0f, 0.0f, -4.0f);
			}
			else if(type.equals("medium_parallel")){
				GL11.glRotatef(-90, 0, 1, 0);
			}
		}
		else if (tcRail.getFacing() == 1) {
			if(type.equals("medium")){
				GL11.glRotatef(180, 0, 1, 0);
				GL11.glTranslatef(-1.0f, 0.0f, 1.0f);
			}
			else if(type.equals("large_90")){
				GL11.glRotatef(180, 0, 1, 0);
				GL11.glTranslatef(0.0f, 0.0f, -4.0f);
			}
			else if(type.equals("medium_parallel")){
				GL11.glRotatef(90, 0, 1, 0);
			}
		}
		else if(tcRail.getFacing() == 2){
			if(type.equals("medium")){
				GL11.glRotatef(90, 0, 1, 0);
				GL11.glTranslatef(-1.0f, 0.0f, 1.0f);
			}
			else if(type.equals("large_90")){
				GL11.glRotatef(90, 0, 1, 0);
				GL11.glTranslatef(0.0f, 0.0f, -4.0f);
			}
			//if(type.equals("medium_parallel")){
				//do something if needed
			//}
		}
		else if(tcRail.getFacing() == 0){
			if(type.equals("medium")){
				GL11.glRotatef(-90, 0, 1, 0);
				GL11.glTranslatef(-1.0f, 0.0f, 1.0f);
			}
			else if(type.equals("large_90")){
				GL11.glRotatef(-90, 0, 1, 0);
				GL11.glTranslatef(.0f, 0.0f, -4.0f);
			}
			else if(type.equals("medium_parallel")){
				GL11.glRotatef(180, 0, 1, 0);
			}
		}
		if(tcRail.getSwitchState()){
			if(type.equals("medium")){this.renderMediumActive();}
			else if(type.equals("medium_parallel")){this.renderMediumParallelActive();}
			else if(type.equals("large_90")){this.renderLarge90Active();}
		} else {
			if(type.equals("medium")){this.renderMediumInactive();}
			else if(type.equals("medium_parallel")){this.renderMediumParallelInactive();}
			else if(type.equals("large_90")){this.renderLarge90Inactive();}
		}
		//if(type.equals("large"))this.renderLarge();
	}
}