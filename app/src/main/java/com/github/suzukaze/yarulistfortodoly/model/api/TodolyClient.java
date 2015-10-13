/*
 * Copyright (C) 2015 Jun Hiroe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.suzukaze.yarulistfortodoly.model.api;

import com.github.suzukaze.yarulistfortodoly.model.Item;
import com.github.suzukaze.yarulistfortodoly.model.Project;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

public class TodolyClient {

  private static final String TAG = TodolyClient.class.getSimpleName();

  public static final String ENDPOINT = "https://todo.ly/";

  public interface TodolyService {

    @GET("/api/authentication/isauthenticated.json")
    Observable<Boolean> isAuthenticated();

    @GET("/api/projects.json")
    Observable<List<Project>> getProjects();

    @POST("/api/projects.json")
    Observable<Project> createProject(@Body Project project);

    @POST("/api/projects/{id}.json")
    Observable<Project> updateProject(@Path("id") long id, @Body Project project);

    @DELETE("/api/projects/{id}.json")
    Observable<Project> deleteProject(@Path("id") long id);

    @GET("/api/items.json")
    Observable<List<Item>> getItems();

    /**
     *
     * @param item set value to item.content
     * @return
     */
    @POST("/api/items.json")
    Observable<Item> createItem(@Body Item item);

    /**
     *
     * @param item set value to item.checked
     * @return
     */
    @POST("/api/items/{id}.json")
    Observable<Item> updateItem(@Path("id") long id, @Body Item item);

    @DELETE("/api/items/{id}.json")
    Observable<Item> deleteItem(@Path("id") long id);
  }
}
