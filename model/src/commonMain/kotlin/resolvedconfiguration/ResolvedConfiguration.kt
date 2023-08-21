/*
 * Copyright (C) 2023 The ORT Project Authors (See <https://github.com/oss-review-toolkit/ort-server/blob/main/NOTICE>)
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

package org.ossreviewtoolkit.server.model.resolvedconfiguration

import org.ossreviewtoolkit.server.model.AnalyzerJob
import org.ossreviewtoolkit.server.model.runs.repository.PackageConfiguration
import org.ossreviewtoolkit.server.model.runs.repository.PackageCuration
import org.ossreviewtoolkit.server.model.runs.repository.Resolutions

/**
 * The resolved configuration for an ORT run. This contains configuration resolved during a job, for example the
 * [PackageCuration]s resolved during an [AnalyzerJob], to ensure that all configuration is resolved only once during an
 * ORT run and that all jobs use the same configuration.
 */
data class ResolvedConfiguration(
    /** The resolved [PackageConfiguration]s. */
    val packageConfigurations: List<PackageConfiguration>,

    /**
     * The resolved [PackageCuration]s for all enabled [provider][PackageCurationProviderConfig]s, ordered by highest
     * priority first.
     */
    val packageCurations: List<ResolvedPackageCurations>,

    /** The resolved [Resolutions]. */
    val resolutions: Resolutions
)
