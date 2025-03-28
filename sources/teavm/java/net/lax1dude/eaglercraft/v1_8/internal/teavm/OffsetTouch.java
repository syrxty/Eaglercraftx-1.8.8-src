/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformInput;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.SortedTouchEvent.ITouchUIDMapper;

public class OffsetTouch {

	public final Touch touch;
	public final int eventUID;
	public final int posX;
	public final int posY;
	public final float radius;

	public OffsetTouch(Touch touch, int eventUID, int posX, int posY, float radius) {
		this.touch = touch;
		this.eventUID = eventUID;
		this.posX = posX;
		this.posY = posY;
		this.radius = radius;
	}

	public static OffsetTouch create(Touch touch, ITouchUIDMapper mapper, int originX, int originY) {
		double contentScale = PlatformInput.getDPI();
		double d = 5.0 * contentScale;
		OffsetTouch ot = new OffsetTouch(touch, mapper.call(touch.getIdentifier()),
				(int) ((touch.getPageX() - originX) * contentScale),
				PlatformInput.getWindowHeight() - (int) ((touch.getPageY() - originY) * contentScale) - 1,
				(float)(touch.getRadiusXSafe(d) * 0.5 + touch.getRadiusYSafe(d) * 0.5));
		return ot;
	}

}