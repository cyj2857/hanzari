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

        private void WebTab_DocumentCompleted(object sender, WebBrowserDocumentCompletedEventArgs e)
        {
            tabControl.SelectedTab.Text = webTab.DocumentTitle;
        }

        String hancomUrl = "https://www.hancom.com/main/main.do";

        private void Form1_Load(object sender, EventArgs e)
        {
            webBrowser.Navigate(hancomUrl);
            textUrl.Text = hancomUrl;
            webBrowser.DocumentCompleted +=webBrowser_DocumentCompleted; //컨트롤이 문서 로드를 완료할 때 발생
        }

        //뒤로
        private void backwardButton_Click(object sender, EventArgs e)
        {
            WebBrowser web = tabControl.SelectedTab.Controls[0] as WebBrowser;
            if (web != null)
            {
                if (web.CanGoBack)
                {
                    web.GoBack();
                }
            }
        }

        //앞으로
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

        //navigate 버튼 클릭 시 웹페이지 이동
        private void navigateButton_Click(object sender, EventArgs e)
        {
            WebBrowser web = tabControl.SelectedTab.Controls[0] as WebBrowser;
            if (web != null)
                web.Navigate(textUrl.Text);
        }

        //엔터 클릭 시 웹페이지 이동
        private void textUrl_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == (char)13)
            {
                WebBrowser web = tabControl.SelectedTab.Controls[0] as WebBrowser;
                if (web != null)
                {
                    web.Navigate(textUrl.Text);
                }
            }
        }

        WebBrowser webTab = null;

        //New Tab 버튼 클릭 시
        private void newTabButton_Click(object sender, EventArgs e)
        {
            TabPage tab = new TabPage();
            tab.Text = "New Tab"; //임시 탭 이름
            tabControl.Controls.Add(tab);
            tabControl.SelectTab(tabControl.TabCount - 1); //새로운 탭으로 선택하려면 인덱스 번호 때문에 -1해주기
            webTab = new WebBrowser() { ScriptErrorsSuppressed = true }; //브라우저가 스크립트 오류 메시지와 같은 대화 상자 표시 여부
            webTab.Parent = tab;
            webTab.Dock = DockStyle.Fill;
            webTab.Navigate(hancomUrl);
            textUrl.Text = hancomUrl;
            webTab.DocumentCompleted += WebTab_DocumentCompleted; //컨트롤이 문서로드를 완료할 때 발생
        }

        //Close 버튼 클릭 시
        private void closeButton_Click(object sender, EventArgs e)
        {
            tabControl.TabPages.Remove(tabControl.SelectedTab);
        }

        //☆메모장에 텍스트 보내는 기능☆

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
            //메모장 실행이 되어있어야 됨
            Process notepadProcess = Process.GetProcessesByName("notepad")[0];
            //이렇게 하면 안됨 왜?
            //Process proc = Process.Start("notepad");
            //Process notepadProcess = Process.GetProcessById(proc.Id);

            //윈도우 메인 핸들로부터 메모장 핸들을 얻는다, 메모장 textbox는 Edit라고 불린다
            IntPtr notepadTextbox = FindWindowEx(notepadProcess.MainWindowHandle, IntPtr.Zero, "Edit", null);
            //URL 텍스트 보내기
            SendMessage(notepadTextbox, WM_SETTEXT, 0, textUrl.Text);
        }
    }
}
