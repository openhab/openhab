/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/*
* generated by Xtext
*/
package org.openhab.model.persistence.ui.contentassist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemRegistry;
import org.openhab.designer.ui.UIActivator;
/**
 * see http://www.eclipse.org/Xtext/documentation/latest/xtext.html#contentAssist on how to customize content assistant
 */
public class PersistenceProposalProvider extends AbstractPersistenceProposalProvider {

	@Override
	public void completeGroupConfig_Group(EObject model, Assignment assignment,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		super.completeGroupConfig_Group(model, assignment, context, acceptor);

		ItemRegistry registry = (ItemRegistry) UIActivator.itemRegistryTracker.getService();
		if(registry!=null) {
			for(Item item : registry.getItems(context.getPrefix() + "*")) {
				if(item instanceof GroupItem) {
					ICompletionProposal completionProposal = createCompletionProposal(item.getName(), context);
					acceptor.accept(completionProposal);
				}
			}
		}
	}
	
	@Override
	public void completeItemConfig_Item(EObject model, Assignment assignment,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		ItemRegistry registry = (ItemRegistry) UIActivator.itemRegistryTracker.getService();
		if(registry!=null) {
			for(Item item : registry.getItems(context.getPrefix() + "*")) {
				ICompletionProposal completionProposal = createCompletionProposal(item.getName(), context);
				acceptor.accept(completionProposal);
			}
		}
	}
}
