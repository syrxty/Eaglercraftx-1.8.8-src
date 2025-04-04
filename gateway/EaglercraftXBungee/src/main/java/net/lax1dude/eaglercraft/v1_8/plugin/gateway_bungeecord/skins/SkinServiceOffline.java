/*
 * Copyright (c) 2022-2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinPresetEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SkinPacketVersionCache;
import net.md_5.bungee.UserConnection;

public class SkinServiceOffline implements ISkinService {

	public static final int masterRateLimitPerPlayer = 250;

	private static class CachedSkin {

		protected final UUID uuid;
		protected final SkinPacketVersionCache packet;

		protected CachedSkin(UUID uuid, SkinPacketVersionCache packet) {
			this.uuid = uuid;
			this.packet = packet;
		}

	}

	private final ConcurrentMap<UUID, CachedSkin> skinCache = new ConcurrentHashMap<>();

	public void init(String uri, String driverClass, String driverPath, int keepObjectsDays, int keepProfilesDays,
			int maxObjects, int maxProfiles) {
		skinCache.clear();
	}

	public void processGetOtherSkin(UUID searchUUID, UserConnection sender) {
		EaglerInitialHandler initialHandler = (EaglerInitialHandler)sender.getPendingConnection();
		if(initialHandler.skinLookupRateLimiter.rateLimit(masterRateLimitPerPlayer)) {
			CachedSkin cached = skinCache.get(searchUUID);
			if(cached != null) {
				initialHandler.sendEaglerMessage(cached.packet.get(initialHandler.getEaglerProtocol()));
			}else {
				initialHandler.sendEaglerMessage(new SPacketOtherSkinPresetEAG(searchUUID.getMostSignificantBits(),
						searchUUID.getLeastSignificantBits(), (searchUUID.hashCode() & 1) != 0 ? 1 : 0));
			}
		}
	}

	public void processGetOtherSkin(UUID searchUUID, String skinURL, UserConnection sender) {
		EaglerInitialHandler initialHandler = (EaglerInitialHandler)sender.getPendingConnection();
		if(skinURL.startsWith("eagler://")) { // customs skulls from exported singleplayer worlds
			initialHandler.sendEaglerMessage(new SPacketOtherSkinPresetEAG(searchUUID.getMostSignificantBits(),
					searchUUID.getLeastSignificantBits(), 0));
			return;
		}
		initialHandler.sendEaglerMessage(new SPacketOtherSkinPresetEAG(searchUUID.getMostSignificantBits(),
				searchUUID.getLeastSignificantBits(), (searchUUID.hashCode() & 1) != 0 ? 1 : 0));
	}

	public void registerEaglercraftPlayer(UUID clientUUID, SkinPacketVersionCache generatedPacket, int modelId) {
		skinCache.put(clientUUID, new CachedSkin(clientUUID, generatedPacket));
	}

	public void unregisterPlayer(UUID clientUUID) {
		skinCache.remove(clientUUID);
	}

	public void registerTextureToPlayerAssociation(String textureURL, UUID playerUUID) {
	}

	public void registerTextureToPlayerAssociation(UUID textureUUID, UUID playerUUID) {
	}

	public void processForceSkin(UUID playerUUID, EaglerInitialHandler initialHandler) {
		CachedSkin cached = skinCache.get(playerUUID);
		if(cached != null) {
			initialHandler.sendEaglerMessage(cached.packet.getForceClientV4());
		}
	}

	public void flush() {
		// no
	}

	public void shutdown() {
		skinCache.clear();
	}

	public SkinPacketVersionCache getSkin(UUID playerUUID) {
		CachedSkin cached = skinCache.get(playerUUID);
		return cached != null ? cached.packet : null;
	}

}