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
public class ModelMediumStraightTCTrack extends ModelBase {
	public ModelRendererTurbo modelSmallStraight = new ModelRendererTurbo(this).addObj("track_normal.obj");
	public ModelMediumStraightTCTrack() {
	}

	public void render() {
		modelSmallStraight.render();
	}

	public void render(TileTCRail tcRail, double x, double y, double z) {

		// Bind the texture, so that OpenGL properly textures our block.
		Tessellator.bindTexture(new ResourceLocation(Info.resourceLocation, Info.modelTexPrefix + "track_normal.png"));
		GL11.glColor4f(1, 1, 1, 1);
		//GL11.glScalef(0.5f, 0.5f, 0.5f);

		switch (tcRail.getFacing()){
			case 3:{GL11.glRotatef(-90, 0, 1, 0);break;}
			case 1:{GL11.glRotatef(90, 0, 1, 0);break;}
			case 0:{GL11.glRotatef(180, 0, 1, 0);}
		}
		for (int i = 0; i < 3; i++) {
			render();
			GL11.glTranslatef(0.0f, 0.0f, -1.0f);
		}
	}
}