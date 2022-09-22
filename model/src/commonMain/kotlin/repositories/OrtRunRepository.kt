/*
 * Copyright (C) 2022 The ORT Project Authors (See <https://github.com/oss-review-toolkit/ort-server/blob/main/NOTICE>)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * License-Filename: LICENSE
 */

package org.ossreviewtoolkit.server.model.repositories

import org.ossreviewtoolkit.server.model.JobConfigurations
import org.ossreviewtoolkit.server.model.OrtRun
import org.ossreviewtoolkit.server.model.OrtRunStatus
import org.ossreviewtoolkit.server.model.util.OptionalValue

/**
 * A repository of [ORT runs][OrtRun].
 */
interface OrtRunRepository {
    /**
     * Create an ORT run.
     */
    fun create(repositoryId: Long, revision: String, jobConfigurations: JobConfigurations): OrtRun

    /**
     * Get an ORT run by [id]. Returns null if the ORT run is not found.
     */
    fun get(id: Long): OrtRun?

    /**
     * List all ORT runs for a [repository][repositoryId].
     */
    fun listForRepository(repositoryId: Long): List<OrtRun>

    /**
     * Update an ORT run by [id] with the [present][OptionalValue.Present] values.
     */
    fun update(
        id: Long,
        status: OptionalValue<OrtRunStatus> = OptionalValue.Absent
    ): OrtRun

    /**
     * Delete an ORT run by [id].
     */
    fun delete(id: Long)
}
