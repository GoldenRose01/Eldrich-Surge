# Release versioning

- Increment the final numeric component of the mod version by one only when the user explicitly requests a JAR. Ordinary edits do not change the version.
- If the user explicitly specifies a version, use that exact version and treat it as the base for later increments.
- Example: from 1.4.1, the next JAR request without an explicit version produces 1.4.2.

# Snapshot releases

- Include a JAR for the latest available Minecraft 26.4 snapshot when preparing releases. Verify the latest snapshot from Mojang's version manifest and compatible Fabric API each time; do not assume the previously used snapshot is still current.

