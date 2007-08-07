/*
 * Created on May 29, 2007
 *
 * Copyright 2007 Chris Nelson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.trails.component.blob;

import java.util.Set;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.valid.IValidationDelegate;
import org.jmock.Mock;
import org.trails.callback.CollectionCallback;
import org.trails.callback.EditCallback;
import org.trails.component.ComponentTest;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.page.EditPage;
import org.trails.page.PageResolver;
import org.trails.page.TrailsPage;
import org.trails.page.TrailsPage.PageType;
import org.trails.testhibernate.UploadableMedia;
import org.trails.testhibernate.UploadableMediaDelegate;

public class EditBlobTest extends ComponentTest {
	IClassDescriptor classDescriptor;
	IdentifierDescriptor idDescriptor;

	EditPage blobEditPage;

	UploadableMedia document;

	Mock cycleMock = new Mock(IRequestCycle.class);
	Mock pageMock = new Mock(IPage.class);
	Mock pageResolverMock = new Mock(PageResolver.class);
	Mock validatorMock = new Mock(IValidationDelegate.class);

	EditCallback editCallback;

	public static final String EDIT_PAGE_NAME = "BlobEdit";
	public static final String DELEGATE_EDIT_PAGE_NAME = "DelegateEdit";
	public static final String EDIT_CALLBACK_NAME = EDIT_PAGE_NAME + "Callback";
	public static final String DELEGATEEDIT_CALLBACK_NAME = DELEGATE_EDIT_PAGE_NAME
			+ "Callback";

	public void setUp() {
		classDescriptor = new TrailsClassDescriptor(UploadableMedia.class);
		idDescriptor = new IdentifierDescriptor(UploadableMedia.class, "id",
				UploadableMedia.class);

		classDescriptor.getPropertyDescriptors().add(idDescriptor);

		document = new UploadableMedia();

		editCallback = new EditCallback(EDIT_CALLBACK_NAME, document);

		blobEditPage = buildEditPage();
		blobEditPage.setModel(document);
		blobEditPage.setPageName(EDIT_PAGE_NAME);

		document = new UploadableMedia();
		document.setFileName(EDIT_PAGE_NAME + "Doc");
		document.setFilePath("C:\\temp");
		document.setFileExtension(".doc");
		document.setDescription(EDIT_PAGE_NAME);
		document.setMediaType(UploadableMedia.EMedia.DOCUMENT);
		document.setContentType("application/msword");
		document.setName(document.getFileName());
		document.setNumBytes(0L);
		byte[] buf = new byte[4096];
		document.setBytes(buf);
	}

	public void testOnFormSubmit() {
		Mock callbackMock = new Mock(ICallback.class);
		callbackMock.expects(once()).method("performCallback").with(
				isA(IRequestCycle.class));
		blobEditPage.setNextPage((ICallback) callbackMock.proxy());
		blobEditPage.onFormSubmit((IRequestCycle) cycleMock.proxy());
		callbackMock.verify();
	}

	public void testActivateExternalPage() throws Exception {
		blobEditPage.setModel(null);
		IExternalPage externalEditPage = (IExternalPage) blobEditPage;
		externalEditPage.activateExternalPage(new Object[] { document },
				(IRequestCycle) cycleMock.proxy());
		assertEquals(document, blobEditPage.getModel());
	}

	public void testGetPropertyDescriptors() {
		descriptorServiceMock.expects(once()).method("getClassDescriptor")
				.with(same(UploadableMedia.class)).will(
						returnValue(classDescriptor));

		assertEquals("got descriptors", classDescriptor, blobEditPage
				.getClassDescriptor());
		descriptorServiceMock.verify();
	}

	public void testfakePage() throws Exception {
		descriptorServiceMock.expects(atLeastOnce()).method(
				"getClassDescriptor").with(eq(UploadableMedia.class)).will(
				returnValue(classDescriptor));

		blobEditPage.getCallbackStack().push(editCallback);
		PageEvent fakePageEvent = new PageEvent(blobEditPage,
				(IRequestCycle) cycleMock.proxy());
		blobEditPage.pageBeginRender(fakePageEvent);
	}

	public void testGetTitle() throws Exception {
		IPropertyDescriptor collectionPropertyDescriptor = new TrailsPropertyDescriptor(
				UploadableMedia.class, "documents", Set.class);
		CollectionDescriptor documentsDescriptor = new CollectionDescriptor(
				UploadableMedia.class, collectionPropertyDescriptor);

		IClassDescriptor doc1 = new TrailsClassDescriptor(
				UploadableMedia.class, "Doc1");
		IClassDescriptor doc2 = new TrailsClassDescriptor(
				UploadableMedia.class, "Doc2");

		descriptorServiceMock.expects(atLeastOnce()).method(
				"getClassDescriptor").with(eq(UploadableMedia.class)).will(
				returnValue(doc2));

		assertEquals("Edit Doc2", blobEditPage.getTitle());

		CollectionCallback collectionCallback = new CollectionCallback(
				EDIT_CALLBACK_NAME, doc1, documentsDescriptor);
		blobEditPage.getCallbackStack().push(collectionCallback);
		EditCallback newCallback = new EditCallback(EDIT_CALLBACK_NAME, doc1);
		blobEditPage.getCallbackStack().push(newCallback);
		assertFalse(newCallback.equals(collectionCallback));
		doc2.getPropertyDescriptors().add(
				new IdentifierDescriptor(UploadableMedia.class, "id",
						UploadableMedia.class));
		assertEquals("Add Doc2", blobEditPage.getTitle());
	}

	public void testCallback() {
		EditPage addPage = (EditPage) creator.newInstance(EditPage.class);
		EditCallback editCallback = new EditCallback(EDIT_PAGE_NAME, document);

		Mock cycleMock = new Mock(IRequestCycle.class);
		cycleMock.expects(once()).method("getPage").with(eq(EDIT_PAGE_NAME))
				.will(returnValue(addPage));
		cycleMock.expects(once()).method("activate").with(same(addPage));
		editCallback.performCallback((IRequestCycle) cycleMock.proxy());
	}

	public void testGetPageName() {
		Mock pageMock = new Mock(IPage.class);
		Mock pageResolverMock = new Mock(PageResolver.class);
		Mock cycleMock = new Mock(IRequestCycle.class);
		pageResolverMock.expects(once()).method("resolvePage").with(
				isA(IRequestCycle.class), eq(UploadableMedia.class),
				eq(TrailsPage.PageType.Edit)).will(
				returnValue(pageMock.proxy()));
		pageMock.expects(once()).method("getPageName").will(
				returnValue(EDIT_PAGE_NAME));
		pageMock.expects(once()).method("getRequestCycle").will(
				returnValue(cycleMock.proxy()));

		EditPage blobEditPage = buildEditPage();
		blobEditPage.setModel(document);
		blobEditPage.setPageName(EDIT_PAGE_NAME);

		UploadableMedia newDoc = new UploadableMedia();
		newDoc.setFileName("NewDoc");
		newDoc.setFilePath("C:\\temp");
		newDoc.setFileExtension(".doc");
		newDoc.setDescription(EDIT_PAGE_NAME);
		newDoc.setMediaType(UploadableMedia.EMedia.DOCUMENT);
		newDoc.setContentType("application/msword");
		newDoc.setName(newDoc.getFileName());
		newDoc.setNumBytes(0L);
		byte[] buf = new byte[4096];
		newDoc.setBytes(buf);
		blobEditPage.setModel(newDoc);

		assertEquals(EDIT_PAGE_NAME, blobEditPage.getPageName());
	}

	public void testEdit() throws Exception {
		UploadableMedia newDoc = new UploadableMedia();
		newDoc.setFileName("NewDoc");
		newDoc.setFilePath("C:\\temp");
		newDoc.setFileExtension(".doc");
		newDoc.setDescription("NewDoc");
		newDoc.setMediaType(UploadableMedia.EMedia.DOCUMENT);
		newDoc.setContentType("application/msword");
		newDoc.setName(newDoc.getFileName());
		newDoc.setNumBytes(0L);
		byte[] buf = new byte[4096];
		newDoc.setBytes(buf);

		EditPage blobEditPage = buildEditPage();
		blobEditPage.setPageName(EDIT_PAGE_NAME);
		blobEditPage.setModel(newDoc);

		Mock pageResolverMock = new Mock(PageResolver.class);
		pageResolverMock.expects(atLeastOnce()).method("resolvePage").with(
				isA(IRequestCycle.class), eq(UploadableMedia.class),
				eq(PageType.Edit)).will(returnValue(blobEditPage));

		Mock cycleMock = new Mock(IRequestCycle.class);
		cycleMock.expects(once()).method("getPage").with(
				eq(blobEditPage.getPageName())).will(returnValue(blobEditPage));
		cycleMock.expects(once()).method("activate").with(same(blobEditPage));

		Mock pageMock = new Mock(IPage.class);
		pageMock.expects(atLeastOnce()).method("getRequestCycle").will(
				returnValue(cycleMock.proxy()));

		EditCallback editCallback = new EditCallback(
				blobEditPage.getPageName(), newDoc);
		editCallback.performCallback((IRequestCycle) cycleMock.proxy());

		persistenceMock.expects(once()).method("save").with(same(newDoc)).will(
				returnValue(newDoc));
		blobEditPage.save((IRequestCycle) cycleMock.proxy());
		assertEquals(newDoc, blobEditPage.getModel());
	}

	public void testRemove() throws Exception {
		UploadableMediaDelegate delegate = new UploadableMediaDelegate();

		UploadableMedia newDoc = new UploadableMedia();
		newDoc.setFileName("NewDoc");
		newDoc.setFilePath("C:\\temp");
		newDoc.setFileExtension(".doc");
		newDoc.setDescription("NewDoc");
		newDoc.setMediaType(UploadableMedia.EMedia.DOCUMENT);
		newDoc.setContentType("application/msword");
		newDoc.setName(newDoc.getFileName());
		newDoc.setNumBytes(0L);
		byte[] buf = new byte[4096];
		newDoc.setBytes(buf);
		delegate.setDocument(document);

		EditPage blobEditPage = buildEditPage();
		blobEditPage.setPageName(EDIT_PAGE_NAME);
		blobEditPage.setModel(newDoc);
		EditCallback editCallback = new EditCallback(
				blobEditPage.getPageName(), newDoc);

		EditPage delegateEditPage = buildEditPage();
		delegateEditPage.setPageName(DELEGATE_EDIT_PAGE_NAME);
		delegateEditPage.setModel(delegate);
		EditCallback delegateCallback = new EditCallback(delegateEditPage
				.getPageName(), delegate);

		blobEditPage.getCallbackStack().push(editCallback);
		delegateEditPage.getCallbackStack().push(delegateCallback);

		Mock pageResolverMock = new Mock(PageResolver.class);
		pageResolverMock.expects(atLeastOnce()).method("resolvePage").with(
				isA(IRequestCycle.class), eq(UploadableMedia.class),
				eq(PageType.Edit)).will(returnValue(blobEditPage));

		Mock cycleMock = new Mock(IRequestCycle.class);

		cycleMock.expects(once()).method("getPage").with(
				eq(delegateEditPage.getPageName())).will(returnValue(delegateEditPage));
		cycleMock.expects(once()).method("activate").with(same(delegateEditPage));

		Mock pageMock = new Mock(IPage.class);
		pageMock.expects(atLeastOnce()).method("getRequestCycle").will(
				returnValue(cycleMock.proxy()));

		persistenceMock.expects(once()).method("save").with(same(delegate))
				.will(returnValue(delegate));
		delegateEditPage.save((IRequestCycle) cycleMock.proxy());

		assertEquals(delegate, delegateEditPage.getModel());
		assertEquals(delegate.getDocument(),
				((UploadableMediaDelegate) delegateEditPage.getModel())
						.getDocument());

		pageResolverMock.expects(atLeastOnce()).method("resolvePage").with(
		isA(IRequestCycle.class), eq(UploadableMediaDelegate.class),
		eq(PageType.Edit)).will(returnValue(delegateEditPage));

		cycleMock.expects(once()).method("getPage").with(
		eq(blobEditPage.getPageName())).will(returnValue(blobEditPage));
		cycleMock.expects(once()).method("activate").with(same(blobEditPage));

		persistenceMock.expects(once()).method("remove").with(same(newDoc));
		persistenceMock.expects(once()).method("save").with(eq(delegate));
		blobEditPage.remove((IRequestCycle) cycleMock.proxy());
		delegateEditPage.save((IRequestCycle) cycleMock.proxy());

		assertNull(delegateEditPage.getModel());
	}
}