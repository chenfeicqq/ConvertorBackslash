package com.chenfei.eclipse.plugin.convertor.backslash;

import java.lang.reflect.Method;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IBlockTextSelection;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.jface.text.JFaceTextUtil;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.texteditor.AbstractTextEditor;

/**
 * 将选中的字符中的‘\’转换成‘/’
 * 
 * @author chenfei
 *
 */
public class ConvertorBackslashAction implements IEditorActionDelegate, IWorkbenchWindowActionDelegate
{
    @Override
    public void run(IAction action)
    {
        final ISourceViewer iSourceViewer = this.getActiveSourceViewer();

        if (null == iSourceViewer)
        {
            return;
        }

        final IDocument document = iSourceViewer.getDocument();

        if (null == document)
        {
            return;
        }

        final StyledText styledText = iSourceViewer.getTextWidget();

        if (null == styledText)
        {
            return;
        }

        final ISelection iSelection = iSourceViewer.getSelectionProvider().getSelection();

        if (!(iSelection instanceof ITextSelection))
        {
            return;
        }

        final ITextSelection iTextSelection = (ITextSelection) iSelection;

        try
        {
            if (JFaceTextUtil.isEmpty(iSourceViewer, iTextSelection))
            {
                return;
            }

            final IRegion[] ranges = JFaceTextUtil.getCoveredRanges(iSourceViewer, iTextSelection);

            if (ranges.length > 1 && iSourceViewer instanceof ITextViewerExtension)
            {
                final ITextViewerExtension iTextViewerExtension = (ITextViewerExtension) iSourceViewer;

                iTextViewerExtension.getRewriteTarget().beginCompoundChange();
            }

            for (final IRegion region : ranges)
            {
                String target = document.get(region.getOffset(), region.getLength());

                if (target.indexOf('\\') > 0)
                {
                    document.replace(region.getOffset(), region.getLength(), target.replace('\\', '/'));
                }
            }

            if (ranges.length > 1 && iSourceViewer instanceof ITextViewerExtension)
            {
                final ITextViewerExtension iTextViewerExtension = (ITextViewerExtension) iSourceViewer;

                iTextViewerExtension.getRewriteTarget().endCompoundChange();
            }
        }
        catch (BadLocationException e)
        {
            // ignore and return
            return;
        }

        // reinstall selection and move it into view
        if (iTextSelection instanceof IBlockTextSelection)
        {
            iSourceViewer.getSelectionProvider().setSelection(iTextSelection);
        }
        else
        {
            iSourceViewer.setSelectedRange(iTextSelection.getOffset(), iTextSelection.getLength());
        }

        // don't use the viewer's reveal feature in order to avoid jumping around
        styledText.showSelection();
    }

    @Override
    public void setActiveEditor(IAction action, IEditorPart targetEditor)
    {
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection)
    {
    }

    @Override
    public void dispose()
    {
        this.iworkbenchwindow = null;
    }

    @Override
    public void init(IWorkbenchWindow iworkbenchwindow)
    {
        this.iworkbenchwindow = iworkbenchwindow;
    }

    private IWorkbenchWindow iworkbenchwindow;

    public ISourceViewer getActiveSourceViewer()
    {
        IEditorPart iEditorPart = this.iworkbenchwindow.getActivePage().getActiveEditor();

        if (!(iEditorPart instanceof AbstractTextEditor))
        {
            return null;
        }

        AbstractTextEditor abstractTextEditor = (AbstractTextEditor) iEditorPart;

        try
        {
            Class<?> internalTextSelectionClass = Class.forName("org.eclipse.ui.texteditor.AbstractTextEditor");

            Method getSourceViewerMethod = internalTextSelectionClass.getDeclaredMethod("getSourceViewer");

            getSourceViewerMethod.setAccessible(true);

            return (ISourceViewer) getSourceViewerMethod.invoke(abstractTextEditor);
        }
        catch (Throwable e)
        {
            return null;
        }
    }

}