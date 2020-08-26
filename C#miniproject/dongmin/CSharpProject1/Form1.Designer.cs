namespace CSharpProject1
{
    partial class Form1
    {
        /// <summary>
        /// 필수 디자이너 변수입니다.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 사용 중인 모든 리소스를 정리합니다.
        /// </summary>
        /// <param name="disposing">관리되는 리소스를 삭제해야 하면 true이고, 그렇지 않으면 false입니다.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form 디자이너에서 생성한 코드

        /// <summary>
        /// 디자이너 지원에 필요한 메서드입니다. 
        /// 이 메서드의 내용을 코드 편집기로 수정하지 마세요.
        /// </summary>
        private void InitializeComponent()
        {
            this.tabControl = new System.Windows.Forms.TabControl();
            this.tabPage1 = new System.Windows.Forms.TabPage();
            this.webBrowser = new System.Windows.Forms.WebBrowser();
            this.backwardButton = new System.Windows.Forms.Button();
            this.textUrl = new System.Windows.Forms.TextBox();
            this.navigateButton = new System.Windows.Forms.Button();
            this.newTabButton = new System.Windows.Forms.Button();
            this.sendUrlButton = new System.Windows.Forms.Button();
            this.forwardButton = new System.Windows.Forms.Button();
            this.closeButton = new System.Windows.Forms.Button();
            this.tabControl.SuspendLayout();
            this.tabPage1.SuspendLayout();
            this.SuspendLayout();
            // 
            // tabControl
            // 
            this.tabControl.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.tabControl.Controls.Add(this.tabPage1);
            this.tabControl.Location = new System.Drawing.Point(12, 45);
            this.tabControl.Name = "tabControl";
            this.tabControl.SelectedIndex = 0;
            this.tabControl.Size = new System.Drawing.Size(776, 393);
            this.tabControl.TabIndex = 0;
            // 
            // tabPage1
            // 
            this.tabPage1.Controls.Add(this.webBrowser);
            this.tabPage1.Location = new System.Drawing.Point(4, 22);
            this.tabPage1.Name = "tabPage1";
            this.tabPage1.Padding = new System.Windows.Forms.Padding(3);
            this.tabPage1.Size = new System.Drawing.Size(768, 367);
            this.tabPage1.TabIndex = 0;
            this.tabPage1.Text = "tabPage1";
            this.tabPage1.UseVisualStyleBackColor = true;
            // 
            // webBrowser
            // 
            this.webBrowser.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.webBrowser.Location = new System.Drawing.Point(0, 0);
            this.webBrowser.MinimumSize = new System.Drawing.Size(20, 20);
            this.webBrowser.Name = "webBrowser";
            this.webBrowser.ScriptErrorsSuppressed = true;
            this.webBrowser.Size = new System.Drawing.Size(765, 367);
            this.webBrowser.TabIndex = 0;
            this.webBrowser.DocumentCompleted += new System.Windows.Forms.WebBrowserDocumentCompletedEventHandler(this.webBrowser_DocumentCompleted);
            // 
            // backwardButton
            // 
            this.backwardButton.BackColor = System.Drawing.SystemColors.InactiveCaption;
            this.backwardButton.Font = new System.Drawing.Font("굴림", 14.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.backwardButton.Location = new System.Drawing.Point(12, 6);
            this.backwardButton.Name = "backwardButton";
            this.backwardButton.Size = new System.Drawing.Size(33, 33);
            this.backwardButton.TabIndex = 1;
            this.backwardButton.Text = "<";
            this.backwardButton.UseVisualStyleBackColor = false;
            this.backwardButton.Click += new System.EventHandler(this.backwardButton_Click);
            // 
            // textUrl
            // 
            this.textUrl.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.textUrl.Font = new System.Drawing.Font("굴림", 15.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.textUrl.Location = new System.Drawing.Point(90, 6);
            this.textUrl.Name = "textUrl";
            this.textUrl.Size = new System.Drawing.Size(410, 32);
            this.textUrl.TabIndex = 3;
            this.textUrl.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.textUrl_KeyPress);
            // 
            // navigateButton
            // 
            this.navigateButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.navigateButton.BackColor = System.Drawing.SystemColors.InactiveCaption;
            this.navigateButton.Font = new System.Drawing.Font("굴림", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.navigateButton.Location = new System.Drawing.Point(506, 6);
            this.navigateButton.Name = "navigateButton";
            this.navigateButton.Size = new System.Drawing.Size(73, 33);
            this.navigateButton.TabIndex = 4;
            this.navigateButton.Text = "Navigate";
            this.navigateButton.UseVisualStyleBackColor = false;
            this.navigateButton.Click += new System.EventHandler(this.navigateButton_Click);
            // 
            // newTabButton
            // 
            this.newTabButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.newTabButton.BackColor = System.Drawing.SystemColors.InactiveCaption;
            this.newTabButton.Font = new System.Drawing.Font("굴림", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.newTabButton.Location = new System.Drawing.Point(585, 5);
            this.newTabButton.Name = "newTabButton";
            this.newTabButton.Size = new System.Drawing.Size(77, 35);
            this.newTabButton.TabIndex = 5;
            this.newTabButton.Text = "New Tab";
            this.newTabButton.UseVisualStyleBackColor = false;
            this.newTabButton.Click += new System.EventHandler(this.newTabButton_Click);
            // 
            // sendUrlButton
            // 
            this.sendUrlButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.sendUrlButton.BackColor = System.Drawing.SystemColors.InactiveCaption;
            this.sendUrlButton.Font = new System.Drawing.Font("굴림", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.sendUrlButton.Location = new System.Drawing.Point(668, 5);
            this.sendUrlButton.Name = "sendUrlButton";
            this.sendUrlButton.Size = new System.Drawing.Size(77, 35);
            this.sendUrlButton.TabIndex = 6;
            this.sendUrlButton.Text = "Send URL";
            this.sendUrlButton.UseVisualStyleBackColor = false;
            this.sendUrlButton.Click += new System.EventHandler(this.sendUrlButton_Click);
            // 
            // forwardButton
            // 
            this.forwardButton.BackColor = System.Drawing.SystemColors.InactiveCaption;
            this.forwardButton.Font = new System.Drawing.Font("굴림", 14.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.forwardButton.Location = new System.Drawing.Point(51, 6);
            this.forwardButton.Name = "forwardButton";
            this.forwardButton.Size = new System.Drawing.Size(33, 33);
            this.forwardButton.TabIndex = 2;
            this.forwardButton.Text = ">";
            this.forwardButton.UseVisualStyleBackColor = false;
            this.forwardButton.Click += new System.EventHandler(this.forwardButton_Click);
            // 
            // closeButton
            // 
            this.closeButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.closeButton.BackColor = System.Drawing.SystemColors.InactiveCaption;
            this.closeButton.Font = new System.Drawing.Font("굴림", 14.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.closeButton.Location = new System.Drawing.Point(751, 4);
            this.closeButton.Name = "closeButton";
            this.closeButton.Size = new System.Drawing.Size(33, 36);
            this.closeButton.TabIndex = 7;
            this.closeButton.Text = "X";
            this.closeButton.UseVisualStyleBackColor = false;
            this.closeButton.Click += new System.EventHandler(this.closeButton_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.ButtonFace;
            this.ClientSize = new System.Drawing.Size(800, 450);
            this.Controls.Add(this.closeButton);
            this.Controls.Add(this.sendUrlButton);
            this.Controls.Add(this.newTabButton);
            this.Controls.Add(this.navigateButton);
            this.Controls.Add(this.textUrl);
            this.Controls.Add(this.forwardButton);
            this.Controls.Add(this.backwardButton);
            this.Controls.Add(this.tabControl);
            this.Name = "Form1";
            this.Text = "NaiveBrowser";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.tabControl.ResumeLayout(false);
            this.tabPage1.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TabControl tabControl;
        private System.Windows.Forms.TabPage tabPage1;
        private System.Windows.Forms.Button backwardButton;
        private System.Windows.Forms.TextBox textUrl;
        private System.Windows.Forms.Button navigateButton;
        private System.Windows.Forms.Button newTabButton;
        private System.Windows.Forms.WebBrowser webBrowser;
        private System.Windows.Forms.Button sendUrlButton;
        private System.Windows.Forms.Button forwardButton;
        private System.Windows.Forms.Button closeButton;
    }
}

