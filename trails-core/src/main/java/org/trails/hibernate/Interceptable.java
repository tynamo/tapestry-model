/*
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
/*
 * Created on Jun 17, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.trails.hibernate;



/**
 * <b>Created:<b> Jun 17, 2004<br><br>
 *
 * <b>Description:</b><br>
 *
 * <br>
 * <d>Revision History:</b><br>
 * ----------------------------------------------------------------------------------<br>
 * Version            Date            Author        Comments<br>
 * ----------------------------------------------------------------------------------<br>
 * 1.0                Jun 17, 2004            CRD3036        Initial Version.
 *<br> <br>
 * @author CRD3036
 * @version 1.0
 *
 */
public interface Interceptable
{
    public boolean isSaved();

    public void onLoad();

    public void onInsert();
    
    public void onUpdate();
}
