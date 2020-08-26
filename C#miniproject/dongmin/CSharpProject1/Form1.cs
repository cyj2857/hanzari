using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Diagnostics;
using System.Runtime.InteropServices;

namespace CSharpProject1
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void webBrowser_DocumentCompleted(object sender, WebBrowserDocumentCompletedEventArgs e)
        {
            tabControl.SelectedTab.Text = webBrowser.DocumentTitle;
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            webBrowser.Navigate("https://www.hancom.com/main/main.do");
            webBrowser.DocumentCompleted +=webBrowser_DocumentCompleted;
        }


        private void navigateButton_Click(object sender, EventArgs e)
        {
            WebBrowser web = tabControl.SelectedTab.Controls[0] as WebBrowser;
            if (web != null)
                web.Navigate(textUrl.Text);
        }

        WebBrowser webTab = null;

        private void newTabButton_Click(object sender, EventArgs e)
        {
            TabPage tab = new TabPage();
            tab.Text = "New Tab";
            tabControl.Controls.Add(tab);
            tabControl.SelectTab(tabControl.TabCount - 1);
            webTab = new WebBrowser() { ScriptErrorsSuppressed = true };
            webTab.Parent = tab;
            webTab.Dock = DockStyle.Fill;
            webTab.Navigate("https://www.hancom.com/main/main.do");
            textUrl.Text = "https://www.hancom.com/main/main.do";
            webTab.DocumentCompleted += WebTab_DocumentCompleted;

        }

        private void WebTab_DocumentCompleted(object sender, WebBrowserDocumentCompletedEventArgs e)
        {
            tabControl.SelectedTab.Text = webTab.DocumentTitle;
        }

        private void backwardButton_Click(object sender, EventArgs e)
        {
            WebBrowser web = tabControl.SelectedTab.Controls[0] as WebBrowser;
            if(web != null)
            {
                if (web.CanGoBack)
                {
                    web.GoBack();
                }
            }
        }

        private void forwardButton_Click(object sender, EventArgs e)
        {
            WebBrowser web = tabControl.SelectedTab.Controls[0] as WebBrowser;
            if (web != null)
            {
                if (web.CanGoForward)
                {
                    web.GoForward();
                }
            }
        }

        private void textUrl_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == (char)13)
            {
                WebBrowser web = tabControl.SelectedTab.Controls[0] as WebBrowser;
                if (web != null)
                {
                    web.Navigate(textUrl.Text);
                }
                else
                {

                }
            }
        }

        //FindWindowEx include
        [DllImport("user32.dll")]
        public static extern IntPtr FindWindowEx(IntPtr hwndParent, IntPtr hwndChildAfter, string lpszClass, string lpszWindow);

        //SendMessage include
        [DllImport("user32.dll")]
        public static extern int SendMessage(IntPtr hWnd, int uMsg, int wParam, string lParam);

        //메시지를 보낼 창을 나타내는 상수(? 잘이해안됨)
        const int WM_SETTEXT = 0X000C;

        private void sendUrlButton_Click(object sender, EventArgs e)
        {
            //메모장 실행
            Process proc = Process.Start("notepad");
            Process notepadProcess = Process.GetProcessById(proc.Id);
            //Process notepadProcess = Process.GetProcessesByName("notepad")[0];
            //notepadProcess.MainWindowHandle하면 됨

            //윈도우 메인 핸들로부터 메모장 핸들을 얻는다, 메모장 textbox는 Edit라고 불린다
            IntPtr notepadTextbox = FindWindowEx(notepadProcess.MainWindowHandle, IntPtr.Zero, "Edit", null);
            //URL 텍스트 보내기
            SendMessage(notepadTextbox, WM_SETTEXT, 0, textUrl.Text);
        }
    }
}
