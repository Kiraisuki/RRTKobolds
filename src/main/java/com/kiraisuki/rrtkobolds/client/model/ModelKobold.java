package com.kiraisuki.rrtkobolds.client.model;

import org.lwjgl.opengl.GL11;

import com.kiraisuki.rrtkobolds.core.Maths;
import com.kiraisuki.rrtkobolds.entities.EntityKobold;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * Kobold model
 * Alternatively, how to draw a kobold
 * @author kiraisuki
 *
 */
public class ModelKobold extends ModelBase
{
	private Head head;
	
	private ModelRenderer torso;
	
	private Arm leftArm,
				rightArm;

	private Leg leftLeg,
				rightLeg;
	
	private Extremity leftFoot,
					  rightFoot,
					  leftHand,
					  rightHand;
	
	private Tail tail;

	//Left and right are from the point of view of the entity
	//setRotationPoint moves families at (0, 0, 0) by their centers
	//Boxes' origins are at the top right corner (model POV)
	//Adding blocks sets their origin to their rotation point. Set rotation point to center of desired location, then correct block position
	//Rotating an object rotates its axis of alignment too
	public ModelKobold()
	{
		super();
		
		this.textureHeight = 64;
		this.textureWidth = 128;
		
		head = new Head(this);
		
		torso = new ModelRenderer(this, 0, 36);
		torso.addBox(-5.0f, 0.0f, -3.0f, 10, 20, 6, 0.0f);
		torso.setRotationPoint(0.0f, 0.0f, 0.0f);
		
		leftArm = new Arm(this, true);
		leftArm.setPosition(7.0f, 5.0f, 0.0f);
		leftArm.setRotationDegrees(0.0f, 0.0f, -5.0f);
		
		rightArm = new Arm(this, false);
		rightArm.setPosition(-7.0f, 5.0f, 0.0f);
		rightArm.setRotationDegrees(0.0f, 0.0f, 5.0f);
		rightArm.mirror(true);
		
		leftHand = new Extremity(this, true, true);
		leftArm.addChildBottom(leftHand);
		leftHand.setPosition(0.0f, 9.0f, 0.0f);
		leftHand.setRotation(0.0f, -90.0f, 0.0f);
		
		rightHand = new Extremity(this, true, false);
		rightArm.addChildBottom(rightHand);
		rightHand.setPosition(0.0f, 9.0f, 0.0f);
		rightHand.setRotation(0.0f, 90.0f, 0.0f);
		rightHand.mirror(true);
		
		leftLeg = new Leg(this);
		leftLeg.setPosition(4.05f, 20.0f, 0.0f);
		
		rightLeg = new Leg(this);
		rightLeg.setPosition(-2.05f, 20.0f, 0.0f);
		rightLeg.mirror(true);
		
		leftFoot = new Extremity(this, false, true);
		leftLeg.addChildBottom(leftFoot);
		leftFoot.setPosition(0.0f, 6.4f, 0.0f);
		leftFoot.setRotation(-67.5f, 0.0f, 0.0f);
		
		rightFoot = new Extremity(this, false, false);
		rightLeg.addChildBottom(rightFoot);
		rightFoot.setPosition(0.0f, 6.4f, 0.0f);
		rightFoot.setRotation(-67.5f, 0.0f, 0.0f);
		rightFoot.mirror(true);
		
		tail = new Tail(this);
		tail.setPosition(0.0f, 16.0f, 0.0f);
		tail.setRotation(45.0f, 0.0f, 0.0f);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		
		GL11.glPushMatrix();
		
		GL11.glScalef(0.3f, 0.3f, 0.3f);
		GL11.glTranslatef(0.0f, 2.2f, 0.0f);
		
		torso.render(scale);
		leftLeg.render(scale);
		rightLeg.render(scale);
		leftArm.render(scale);
		rightArm.render(scale);
		head.render(scale);
		tail.render(scale);
		
		GL11.glPopMatrix();
	}
	
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		int animDesync = ((EntityKobold)entityIn).ANIM_DESYNC;
		
		//Looking around
		head.setRotationRadians(headPitch * 0.017453292f, netHeadYaw * 0.017453292f, 0.0f);
		
		//Breathing
		head.setMouthAngle((float)Math.max(MathHelper.sin((float) ((animDesync + ageInTicks) * 0.05)) * 0.1f, 0));
		
		//Tail swinging slowly
		tail.articulate(MathHelper.sin((float)(animDesync + ageInTicks) * 0.03f) * 0.1f);
        
		//Arm swinging while walking
        rightArm.setRotationRadians(MathHelper.cos(limbSwing * 0.6662f + (float)Math.PI) * 2.0f * limbSwingAmount * 0.5f, 0.0f, 0.0f);
        leftArm.setRotationRadians(MathHelper.cos(limbSwing * 0.6662f) * 2.0f * limbSwingAmount * 0.5f, 0.0f, 0.0f);   
        
        //Legs walking
        rightLeg.setRotationRadians(MathHelper.cos(limbSwing * 0.6662F) * 1.4f * limbSwingAmount, 0.0f, 0.0f);
        leftLeg.setRotationRadians(MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4f * limbSwingAmount, 0.0f, 0.0f);
	}
	
	private class Arm extends ModelRenderer
	{
		private ModelRenderer foreArm,
							  aftArm;
		
		private final float AFTARM_DEFAULT_ANGLE_X = Maths.toRadians(5.0f);
		private final float AFTARM_DEFAULT_ANGLE_Z;
		
		public Arm(ModelBase model, boolean isLeft)
		{
			super(model);
			
			AFTARM_DEFAULT_ANGLE_Z = Maths.toRadians(isLeft ? -5.0f : 5.0f);
			
			aftArm = new ModelRenderer(model, 50, 22);
			foreArm = new ModelRenderer(model, 66, 22);
			
			aftArm.addChild(foreArm);
			
			aftArm.addBox(-2.0f, -2.0f, -2.0f, 4, 10, 4, 0.0f);
			aftArm.setRotationPoint(0.0f, 0.0f, 0.0f);
			aftArm.rotateAngleX = AFTARM_DEFAULT_ANGLE_X;
			aftArm.rotateAngleZ = AFTARM_DEFAULT_ANGLE_Z;
			
			foreArm.addBox(-2.0f, 0.0f, -2.0f, 4, 10, 4, -0.2f);
			foreArm.setRotationPoint(0.0f, 6.0f, 0.0f);
			foreArm.rotateAngleX = Maths.toRadians(-10.0f);
			foreArm.rotateAngleZ = Maths.toRadians(isLeft ? 5.0f : -5.0f);
		}
		
		public void mirror(boolean m)
		{
			foreArm.mirror = m;
			aftArm.mirror = m;
		}
		
		public void addChild(ModelRenderer child)
		{
			aftArm.addChild(child);
		}
		
		public void addChildBottom(ModelRenderer child)
		{
			foreArm.addChild(child);
		}
		
		public void setPosition(float x, float y, float z)
		{
			aftArm.setRotationPoint(x, y, z);
		}
		
		/**
		 * Sets rotation of the foot. Parameters in degrees
		 * @param x
		 * @param y
		 * @param z
		 */
		public void setRotationDegrees(float x, float y, float z)
		{
			aftArm.rotateAngleX = AFTARM_DEFAULT_ANGLE_X + Maths.toRadians(x);
			aftArm.rotateAngleY = Maths.toRadians(y);
			aftArm.rotateAngleZ = AFTARM_DEFAULT_ANGLE_Z + Maths.toRadians(z);
		}
		
		/**
		 * Sets rotation of the foot. Parameters in radians
		 * @param x
		 * @param y
		 * @param z
		 */
		public void setRotationRadians(float x, float y, float z)
		{
			aftArm.rotateAngleX = AFTARM_DEFAULT_ANGLE_X + x;
			aftArm.rotateAngleY = y;
			aftArm.rotateAngleZ = AFTARM_DEFAULT_ANGLE_Z + z;
		}
		
		public void render(float scale)
		{
			aftArm.render(scale);
		}
	}
	
	private class Tail extends ModelRenderer
	{
		private ModelRenderer segment1,
							  segment2,
							  segment3,
							  segment4,
							  segment5;
		
		public Tail(ModelBase model)
		{
			super(model);
			
			segment1 = new ModelRenderer(model, 83, 22);
			segment2 = new ModelRenderer(model, 83, 22);
			segment3 = new ModelRenderer(model, 83, 22);
			segment4 = new ModelRenderer(model, 83, 22);
			segment5 = new ModelRenderer(model, 83, 40);
			
			segment1.addChild(segment2);
			segment2.addChild(segment3);
			segment3.addChild(segment4);
			segment4.addChild(segment5);
			
			segment1.addBox(-3.0f, -1.0f, -3.0f, 6, 12, 6, 0.0f);
			segment1.setRotationPoint(0.0f, 0.0f, 0.0f);
			
			segment2.addBox(-3.0f, 0.0f, -3.0f, 6, 12, 6, -0.6f);
			segment2.setRotationPoint(0.0f, 10.0f, 0.0f);
			segment2.rotateAngleX = Maths.toRadians(11.25f);
			
			segment3.addBox(-3.0f, 0.0f, -3.0f, 6, 12, 6, -1.2f);
			segment3.setRotationPoint(0.0f, 8.0f, -0.4f);
			segment3.rotateAngleX = Maths.toRadians(11.25f);
			
			segment4.addBox(-3.0f, 0.0f, -3.0f, 6, 12, 6, -1.8f);
			segment4.setRotationPoint(0.0f, 6.0f, -0.8f);
			segment4.rotateAngleX = Maths.toRadians(11.25f);
			
			segment5.addBox(-3.0f, 0.0f, -3.0f, 6, 12, 6, -2.4f);
			segment5.setRotationPoint(0.0f, 5.5f, -1.2f);
			segment5.rotateAngleX = Maths.toRadians(11.25f);
		}
		
		public void setPosition(float x, float y, float z)
		{
			segment1.setRotationPoint(x, y, z);
		}
		
		public void articulate(float radians)
		{
			segment1.rotateAngleZ = radians;
			segment2.rotateAngleZ = segment1.rotateAngleZ;
			segment3.rotateAngleZ = segment2.rotateAngleZ;
			segment4.rotateAngleZ = segment3.rotateAngleZ;
			segment5.rotateAngleZ = segment4.rotateAngleZ;
		}
		
		/**
		 * Sets rotation of the foot. Parameters in degrees
		 * @param x
		 * @param y
		 * @param z
		 */
		public void setRotation(float x, float y, float z)
		{
			segment1.rotateAngleX = Maths.toRadians(x);
			segment1.rotateAngleY = Maths.toRadians(y);
			segment1.rotateAngleZ = Maths.toRadians(z);
		}
		
		public void render(float scale)
		{
			segment1.render(scale);
		}
	}
	
	private class Leg extends ModelRenderer
	{
		private ModelRenderer aftLeg,
							  foreLeg,
							  ankle;
		
		private final float AFTLEG_DEFAULT_ANGLE = Maths.toRadians(-22.5f);
		
		public Leg(ModelBase model)
		{
			super(model);
			
			aftLeg = new ModelRenderer(model, 33, 36);
			aftLeg.addBox(-4.0f, -1.0f, -3.0f, 6, 10, 6, -0.1f);
			aftLeg.setRotationPoint(0.0f, 0.0f, 0.0f);
			aftLeg.rotateAngleX = AFTLEG_DEFAULT_ANGLE;
			
			foreLeg = new ModelRenderer(model, 57, 36);
			aftLeg.addChild(foreLeg);
			foreLeg.addBox(-2.5f, -3.0f, -3.0f, 5, 12, 4, -0.061f);
			foreLeg.setRotationPoint(-1.0f, 9.0f, 1.2f);
			foreLeg.rotateAngleX = Maths.toRadians(45.0f);
			
			ankle = new ModelRenderer(model, 33, 52);
			foreLeg.addChild(ankle);
			ankle.addBox(-2.0f, -1.0f, -1.0f, 4, 8, 2, -0.022f);
			ankle.setRotationPoint(0.0f, 9.0f, -0.8f);
			ankle.rotateAngleX = Maths.toRadians(-45.0f);
		}
		
		public void mirror(boolean m)
		{
			foreLeg.mirror = m;
			aftLeg.mirror = m;
			ankle.mirror = m;
		}
		
		public void addChild(ModelRenderer child)
		{
			aftLeg.addChild(child);
		}
		
		public void addChildBottom(ModelRenderer child)
		{
			ankle.addChild(child);
		}
		
		public void setPosition(float x, float y, float z)
		{
			aftLeg.setRotationPoint(x, y, z);
		}
		
		/**
		 * Sets rotation of the foot. Parameters in degrees
		 * @param x
		 * @param y
		 * @param z
		 */
		public void setRotationDegrees(float x, float y, float z)
		{
			aftLeg.rotateAngleX = AFTLEG_DEFAULT_ANGLE + Maths.toRadians(x);
			aftLeg.rotateAngleY = Maths.toRadians(y);
			aftLeg.rotateAngleZ = Maths.toRadians(z);
		}
		
		/**
		 * Sets rotation of the foot. Parameters in degrees
		 * @param x
		 * @param y
		 * @param z
		 */
		public void setRotationRadians(float x, float y, float z)
		{
			aftLeg.rotateAngleX = AFTLEG_DEFAULT_ANGLE + x;
			aftLeg.rotateAngleY = y;
			aftLeg.rotateAngleZ = z;
		}
		
		public void render(float scale)
		{
			aftLeg.render(scale);
		}
	}
	
	private class Head extends ModelRenderer
	{
		private ModelRenderer head,
							  upperJaw,
							  lowerJaw,
							  upperTeeth,
							  lowerTeeth,
							  leftNostril,
							  rightNostril,
							  leftDecoration,
							  leftDecoration2,
							  leftDecoration3,
							  rightDecoration,
							  rightDecoration2,
							  rightDecoration3,
							  leftHorn,
							  rightHorn,
							  leftHorn2,
							  rightHorn2,
							  leftHorn3,
							  rightHorn3;
		
		public Head(ModelBase model)
		{
			super(model);
			
			head = new ModelRenderer(model, 0, 0);
			head.addBox(-5.0f, -10.0f, -6.0f, 10, 10, 12, 0.0f);
	 		head.setRotationPoint(0.0f, 0.0f, 0.0f);
	 		
	 		upperJaw = new ModelRenderer(model, 45, 0);
	 		head.addChild(upperJaw);
	 		upperJaw.addBox(-4.0f, -6.0f, -14.0f, 8, 4, 10, 0.0f);
	 		upperJaw.setRotationPoint(0.0f, 0.0f, 0.0f);
	 		
	 		upperTeeth = new ModelRenderer(model, 91, 10);
	 		upperJaw.addChild(upperTeeth);
	 		upperTeeth.addBox(-3.0f, -2.0f, -13.0f, 6, 1, 8, -0.025f);
	 		upperTeeth.setRotationPoint(0.0f, 0.0f, 0.0f);
	 		
	 		leftNostril = new ModelRenderer(model, 82, 6);
	 		upperJaw.addChild(leftNostril);
	 		leftNostril.setRotationPoint(1.0f, -7.0f, -13.0f);
	 		leftNostril.addBox(1.0f, -0.4f, -1.5f, 2, 4, 2, -0.02f);
	 		leftNostril.rotateAngleX = Maths.toRadians(67.5f);
	 		
	 		rightNostril = new ModelRenderer(model, 82, 0);
	 		upperJaw.addChild(rightNostril);
	 		rightNostril.setRotationPoint(-3.0f, -7.0f, -13.0f);
	 		rightNostril.addBox(-1.0f, -0.4f, -1.5f, 2, 4, 2, -0.02f);
	 		rightNostril.rotateAngleX = Maths.toRadians(67.5f);
	 		rightNostril.mirror = true;
	 		
	 		lowerJaw = new ModelRenderer(model, 91, 0);
	 		head.addChild(lowerJaw);
	 		lowerJaw.addBox(-1.0f, 0.0f, -7.0f, 6, 2, 8, -0.02f);
	 		lowerJaw.setRotationPoint(-2.0f, -2.0f, -6.0f);
	 		
	 		lowerTeeth = new ModelRenderer(model, 91, 10);
	 		lowerJaw.addChild(lowerTeeth);
	 		lowerTeeth.addBox(-1.0f, -2.0f, -7.0f, 6, 1, 8, 0.0f);
	 		lowerTeeth.setRotationPoint(0.0f, 1.0f, 0.0f);
	 		lowerTeeth.mirror = true;
	 		
	 		leftHorn = new ModelRenderer(model, 0, 22);
	 		head.addChild(leftHorn);
	 		leftHorn.addBox(2.0f, -6.0f, -2.0f, 4, 8, 4, 0.0f);
	 		leftHorn.setRotationPoint(-1.0f, -10.0f, 4.0f);
	 		leftHorn.rotateAngleX = Maths.toRadians(-45.0f);
	 		leftHorn.rotateAngleZ = Maths.toRadians(22.5f);
	 		
	 		leftHorn2 = new ModelRenderer(model, 0, 22);
	 		leftHorn.addChild(leftHorn2);
	 		leftHorn2.addBox(-2.0f, -5.0f, -2.0f, 4, 6, 4, -0.4f);
	 		leftHorn2.setRotationPoint(4.0f, -6.0f, 0.0f);
	 		leftHorn2.rotateAngleX = Maths.toRadians(-22.5f);
	 		
	 		leftHorn3 = new ModelRenderer(model, 16, 22);
	 		leftHorn2.addChild(leftHorn3);
	 		leftHorn3.addBox(-2.0f, -6.0f, -2.0f, 4, 6, 4, -1.0f);
	 		leftHorn3.setRotationPoint(0.0f, -3.0f, -0.6f);
	 		leftHorn3.rotateAngleX = Maths.toRadians(-22.5f);
	 		
	 		rightHorn = new ModelRenderer(model, 0, 22);
	 		head.addChild(rightHorn);
	 		rightHorn.addBox(-6.0f, -6.0f, -2.0f, 4, 8, 4, 0.0f);
	 		rightHorn.setRotationPoint(1.0f, -10.0f, 4.0f);
	 		rightHorn.rotateAngleX = Maths.toRadians(-45.0f);
	 		rightHorn.rotateAngleZ = Maths.toRadians(-22.5f);
	 		rightHorn.mirror = true;
	 		
	 		rightHorn2 = new ModelRenderer(model, 0, 22);
	 		rightHorn.addChild(rightHorn2);
	 		rightHorn2.addBox(-10.0f, -5.0f, -2.0f, 4, 6, 4, -0.4f);
	 		rightHorn2.setRotationPoint(4.0f, -6.0f, 0.0f);
	 		rightHorn2.rotateAngleX = Maths.toRadians(-22.5f);
	 		rightHorn2.mirror = true;
	 		
	 		rightHorn3 = new ModelRenderer(model, 16, 22);
	 		rightHorn2.addChild(rightHorn3);
	 		rightHorn3.addBox(-10.0f, -6.0f, -2.0f, 4, 6, 4, -1.0f);
	 		rightHorn3.setRotationPoint(0.0f, -3.0f, -0.6f);
	 		rightHorn3.rotateAngleX = Maths.toRadians(-22.5f);
	 		rightHorn3.mirror = true;
	 		
	 		leftDecoration = new ModelRenderer(model, 33, 22);
	 		head.addChild(leftDecoration);
	 		leftDecoration.addBox(0.0f, 0.0f, 0.0f, 2, 6, 6, 0.0f);
	 		leftDecoration.setRotationPoint(2.5f, -5.0f, 2.0f);
	 		leftDecoration.rotateAngleX = Maths.toRadians(45.0f);
	 		leftDecoration.rotateAngleY = Maths.toRadians(27.625f);
	 		
	 		leftDecoration2 = new ModelRenderer(model, 50, 14);
	 		leftDecoration.addChild(leftDecoration2);
	 		leftDecoration2.addBox(0.0f, 0.0f, 0.0f, 2, 2, 2, 0.0f);
	 		leftDecoration2.setRotationPoint(0.0f, 0.0f, 6.0f);
	 		
	 		leftDecoration3 = new ModelRenderer(model, 58, 14);
	 		leftDecoration.addChild(leftDecoration3);
	 		leftDecoration3.addBox(0.0f, 0.0f, 0.0f, 2, 2, 2, 0.0f);
	 		leftDecoration3.setRotationPoint(0.0f, 6.0f, 0.0f);
	 		
	 		rightDecoration = new ModelRenderer(model, 33, 22);
	 		head.addChild(rightDecoration);
	 		rightDecoration.addBox(-2.0f, 0.0f, 0.0f, 2, 6, 6, 0.0f);
	 		rightDecoration.setRotationPoint(-2.5f, -5.0f, 2.0f);
	 		rightDecoration.rotateAngleX = Maths.toRadians(45.0f);
	 		rightDecoration.rotateAngleY = Maths.toRadians(-27.625f);
	 		rightDecoration.mirror = true;
	 		
	 		rightDecoration2 = new ModelRenderer(model, 50, 14);
	 		rightDecoration.addChild(rightDecoration2);
	 		rightDecoration2.addBox(-2.0f, 0.0f, 0.0f, 2, 2, 2, 0.0f);
	 		rightDecoration2.setRotationPoint(0.0f, 0.0f, 6.0f);
	 		rightDecoration2.mirror = true;
	 		
	 		rightDecoration3 = new ModelRenderer(model, 58, 14);
	 		rightDecoration.addChild(rightDecoration3);
	 		rightDecoration3.addBox(-2.0f, 0.0f, 0.0f, 2, 2, 2, 0.0f);
	 		rightDecoration3.setRotationPoint(0.0f, 6.0f, 0.0f);
	 		rightDecoration3.mirror = true;
		}
		
		public void setMouthAngle(float radians)
		{
			lowerJaw.rotateAngleX = radians;
		}
		
		public void setPosition(float x, float y, float z)
		{
			head.setRotationPoint(x, y, z);
		}
		
		/**
		 * Sets rotation of the foot. Parameters in degrees
		 * @param x
		 * @param y
		 * @param z
		 */
		public void setRotationDegrees(float x, float y, float z)
		{
			head.rotateAngleX = Maths.toRadians(x);
			head.rotateAngleY = Maths.toRadians(y);
			head.rotateAngleZ = Maths.toRadians(z);
		}
		
		/**
		 * Sets rotation of the foot. Parameters in radians
		 * @param x
		 * @param y
		 * @param z
		 */
		public void setRotationRadians(float x, float y, float z)
		{
			head.rotateAngleX = x;
			head.rotateAngleY = y;
			head.rotateAngleZ = z;
		}
		
		public void render(float scale)
		{
			head.render(scale);
		}
	}
	
	private class Extremity extends ModelRenderer
	{
		private ModelRenderer middleDigit,
		  					  leftDigit,
		  					  rightdigit,
		  					  thumbDigit,
		  					  middleClaw1,
		  					  middleClaw2,
		  					  leftClaw1,
		  					  leftClaw2,
		  					  rightClaw1,
		  					  rightClaw2,
		  					  thumbClaw1,
		  					  thumbClaw2;
		
		private boolean hand;
		
		/**
		 * A kobold extremity
		 * @param model
		 * @param clawLength
		 * @param isHand
		 * @param isLeft
		 */
		public Extremity(ModelBase model, boolean isHand, boolean isLeft)
		{
			super(model);
			
			float clawDist1 = 1.0f;
			float clawDist2 = 2.0f;
			
			hand = isHand;
			
			middleDigit = new ModelRenderer(model, 46, 52);
			leftDigit = new ModelRenderer(model, 46, 52);
			rightdigit = new ModelRenderer(model, 46, 52);
			middleClaw1 = new ModelRenderer(model, 54, 52);
			middleClaw2 = new ModelRenderer(model, 62, 52);
			leftClaw1 = new ModelRenderer(model, 54, 52);
			leftClaw2 = new ModelRenderer(model, 62, 52);
			rightClaw1 = new ModelRenderer(model, 54, 52);
			rightClaw2 = new ModelRenderer(model, 62, 52);
			
			middleDigit.addChild(leftDigit);
			middleDigit.addChild(rightdigit);
			middleDigit.addChild(middleClaw1);
			leftDigit.addChild(leftClaw1);
			rightdigit.addChild(rightClaw1);
			middleClaw1.addChild(middleClaw2);
			leftClaw1.addChild(leftClaw2);
			rightClaw1.addChild(rightClaw2);
			
			if(isHand)
			{
				thumbDigit = new ModelRenderer(model, 46, 52);
				thumbClaw1 = new ModelRenderer(model, 54, 52);
				thumbClaw2 = new ModelRenderer(model, 62, 52);
				
				middleDigit.addChild(thumbDigit);
				thumbDigit.addChild(thumbClaw1);
				thumbClaw1.addChild(thumbClaw2);
				
				if(isLeft)
				{
					thumbDigit.addBox(0.0f, 0.0f, -1.0f, 2, 4, 2, -0.04f);
					thumbDigit.setRotationPoint(-2.5f, -1.0f, 0.0f);
					thumbDigit.rotateAngleZ = Maths.toRadians(36.0f);
				}
				
				else
				{
					thumbDigit.addBox(0.0f, 0.0f, -1.0f, 2, 4, 2, -0.04f);
					thumbDigit.setRotationPoint(0.5f, 0.0f, 0.0f);
					thumbDigit.rotateAngleZ = Maths.toRadians(-36.0f);
				}
				
				thumbClaw1.addBox(-1.0f, 0.0f, -1.0f, 2, 3, 2, -0.4f);
				thumbClaw1.setRotationPoint(1.0f, clawDist1 * 3.4f, -0.25f);
				thumbClaw1.rotateAngleX = Maths.toRadians(11.25f);
				
				thumbClaw2.addBox(-1.0f, 0.0f, -1.0f, 2, 3, 2, -0.6f);
				thumbClaw2.setRotationPoint(0.0f, clawDist2 * 0.8f, 0.0f);
				thumbClaw2.rotateAngleX = Maths.toRadians(11.25f);
			}
			
			middleDigit.addBox(-1.0f, -1.0f, -1.0f, 2, 6, 2, 0.0f);
			middleDigit.setRotationPoint(0.0f, 0.0f, 0.0f);
			
			leftDigit.addBox(0.0f, -1.0f, -1.0f, 2, 6, 2, -0.02f);
			leftDigit.setRotationPoint(0.5f, 0.0f, 0.0f);
			leftDigit.rotateAngleZ = Maths.toRadians(-9.0f);
			
			rightdigit.addBox(0.0f, -1.4f, -1.0f, 2, 6, 2, -0.02f);
			rightdigit.setRotationPoint(-2.5f, 0.0f, 0.0f);
			rightdigit.rotateAngleZ = Maths.toRadians(9.0f);
			
			leftClaw1.addBox(-1.0f, 0.0f, -1.0f, 2, 3, 2, -0.4f);
			leftClaw1.setRotationPoint(1.0f, clawDist1 * 4.4f, -0.25f);
			leftClaw1.rotateAngleX = Maths.toRadians(11.25f);
			
			leftClaw2.addBox(-1.0f, 0.0f, -1.0f, 2, 3, 2, -0.6f);
			leftClaw2.setRotationPoint(0.0f, clawDist2 * 0.8f, 0.0f);
			leftClaw2.rotateAngleX = Maths.toRadians(11.25f);
			
			middleClaw1.addBox(-2.0f, 0.0f, -1.0f, 2, 3, 2, -0.4f);
			middleClaw1.setRotationPoint(1.0f, clawDist1 * 4.4f, -0.25f);
			middleClaw1.rotateAngleX = Maths.toRadians(11.25f);
			
			middleClaw2.addBox(-2.0f, 0.0f, -1.0f, 2, 3, 2, -0.6f);
			middleClaw2.setRotationPoint(0.0f, clawDist2 * 0.8f, 0.0f);
			middleClaw2.rotateAngleX = Maths.toRadians(11.25f);
			
			rightClaw1.addBox(-1.0f, -0.4f, -0.8f, 2, 3, 2, -0.4f);
			rightClaw1.setRotationPoint(1.0f, clawDist1 * 4.4f, -0.25f);
			rightClaw1.rotateAngleX = Maths.toRadians(11.25f);
			
			rightClaw2.addBox(-1.0f, -0.4f, -0.8f, 2, 3, 2, -0.6f);
			rightClaw2.setRotationPoint(0.0f, clawDist2 * 0.8f, 0.0f);
			rightClaw2.rotateAngleX = Maths.toRadians(11.25f);
		}
		
		public void mirror(boolean m)
		{
			middleDigit.mirror = m;
			leftDigit.mirror = m;
			rightdigit.mirror = m;
			middleClaw1.mirror = m;
			middleClaw2.mirror = m;
			leftClaw1.mirror = m;
			leftClaw2.mirror = m;
			rightClaw1.mirror = m;
			rightClaw2.mirror = m;
			
			if(hand)
			{
				thumbDigit.mirror = m;
				thumbClaw1.mirror = m;
				thumbClaw2.mirror = m;
			}
		}
		
		public void setPosition(float x, float y, float z)
		{
			middleDigit.setRotationPoint(x, y, z);
		}
		
		/**
		 * Sets rotation of the foot. Parameters in degrees
		 * @param x
		 * @param y
		 * @param z
		 */
		public void setRotation(float x, float y, float z)
		{
			middleDigit.rotateAngleX = Maths.toRadians(x);
			middleDigit.rotateAngleY = Maths.toRadians(y);
			middleDigit.rotateAngleZ = Maths.toRadians(z);
		}
		
		public void render(float scale)
		{
			middleDigit.render(scale);
		}
	}
}
