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

package org.ossreviewtoolkit.server.workers.analyzer

import java.io.File

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

import org.ossreviewtoolkit.analyzer.managers.Npm
import org.ossreviewtoolkit.model.config.AnalyzerConfiguration
import org.ossreviewtoolkit.model.config.RepositoryConfiguration

import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("org.ossreviewtoolkit.server.workers.analyzer.EntrypointKt")

fun main() {
    // This is the entry point of the Analyzer Docker image. It calls the Analyzer from ORT programmatically by
    // interfacing on its APIs.
    logger.info("Hello World")

    // This tests that ORT's classes can be accessed as well as the CLI tools of the Docker image.
    val npm = Npm.Factory().create(File("."), AnalyzerConfiguration(), RepositoryConfiguration())
    val version = npm.getVersion()
    logger.info("Npm version is $version.")

    // Reading environment variables, which could be set e.g. in a docker compose file. Otherwise, use default
    // values. This is only an experimental approach to get access to ORT server specific environment variables,
    // which could be improved by using a configuration file.
    val host = System.getenv("ORT_SERVER_URL") ?: "http://localhost:8080"
    val user = System.getenv("ORT_SERVER_USER") ?: "admin"
    val password = System.getenv("ORT_SERVER_PASSWORD") ?: "admin"
    val authUrl = System.getenv("ORT_SERVER_AUTH_URL")
        ?: "http://localhost:8081/realms/master/protocol/openid-connect/token"
    val clientId = System.getenv("ORT_SERVER_CLIENT_ID") ?: "ort-server"
    logger.info("ORT server base URL: $host")
    logger.info("ORT server user: $user")
    logger.info("ORT server authentication URL: $authUrl")
    logger.info("ORT server client ID: $clientId")

    runBlocking {
        val client = ServerClient.create(host, user, password, clientId, authUrl)

        while (true) {
            delay(10 * 1000)

            client.getScheduledAnalyzerJob()?.let { startedJob ->
                logger.info("Analyzer job with id '${startedJob.id}' started at ${startedJob.startedAt}.")
                logger.info("Running...")
                delay(10 * 1000)
                client.finishAnalyzerJob(startedJob.id)?.let { finishedJob ->
                    logger.info("Analyzer job with id '${finishedJob.id} finished at ${finishedJob.finishedAt}")
                }
            }
        }
    }
}
