namespace HyeonhoApp
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
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Form1));
            this.menuStrip1 = new System.Windows.Forms.MenuStrip();
            this.파일FToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.새로만들기ToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.열기OToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.저장SToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.도움말ToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.도움말보기HToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.피드백보내기ToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator1 = new System.Windows.Forms.ToolStripSeparator();
            this.프로그램정보IToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.splitContainer1 = new System.Windows.Forms.SplitContainer();
            this.button_reset = new System.Windows.Forms.Button();
            this.button1 = new System.Windows.Forms.Button();
            this.monthCalendar1 = new System.Windows.Forms.MonthCalendar();
            this.label_nowTime = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.label_scheduleName = new System.Windows.Forms.Label();
            this.comboBox_endTime = new System.Windows.Forms.ComboBox();
            this.comboBox_startTime = new System.Windows.Forms.ComboBox();
            this.textBox_scheduleContents = new System.Windows.Forms.TextBox();
            this.textBox_scheduleName = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.button_addSchedule = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.button_scheduleModify = new System.Windows.Forms.Button();
            this.button_deleteSchedule = new System.Windows.Forms.Button();
            this.listView_schedule = new System.Windows.Forms.ListView();
            this.date = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.time = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.scheduleName = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.scheduleContents = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.label3 = new System.Windows.Forms.Label();
            this.timer1 = new System.Windows.Forms.Timer(this.components);
            this.menuStrip1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).BeginInit();
            this.splitContainer1.Panel1.SuspendLayout();
            this.splitContainer1.Panel2.SuspendLayout();
            this.splitContainer1.SuspendLayout();
            this.SuspendLayout();
            // 
            // menuStrip1
            // 
            this.menuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.파일FToolStripMenuItem,
            this.도움말ToolStripMenuItem});
            this.menuStrip1.Location = new System.Drawing.Point(0, 0);
            this.menuStrip1.Name = "menuStrip1";
            this.menuStrip1.Size = new System.Drawing.Size(769, 24);
            this.menuStrip1.TabIndex = 1;
            this.menuStrip1.Text = "menuStrip1";
            this.menuStrip1.ItemClicked += new System.Windows.Forms.ToolStripItemClickedEventHandler(this.menuStrip1_ItemClicked);
            // 
            // 파일FToolStripMenuItem
            // 
            this.파일FToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.새로만들기ToolStripMenuItem,
            this.열기OToolStripMenuItem,
            this.저장SToolStripMenuItem});
            this.파일FToolStripMenuItem.Name = "파일FToolStripMenuItem";
            this.파일FToolStripMenuItem.Size = new System.Drawing.Size(57, 20);
            this.파일FToolStripMenuItem.Text = "파일(&F)";
            // 
            // 새로만들기ToolStripMenuItem
            // 
            this.새로만들기ToolStripMenuItem.Name = "새로만들기ToolStripMenuItem";
            this.새로만들기ToolStripMenuItem.Size = new System.Drawing.Size(180, 22);
            this.새로만들기ToolStripMenuItem.Text = "새로 만들기(&N)";
            this.새로만들기ToolStripMenuItem.Click += new System.EventHandler(this.새로만들기ToolStripMenuItem_Click);
            // 
            // 열기OToolStripMenuItem
            // 
            this.열기OToolStripMenuItem.Name = "열기OToolStripMenuItem";
            this.열기OToolStripMenuItem.Size = new System.Drawing.Size(180, 22);
            this.열기OToolStripMenuItem.Text = "열기(&O)";
            this.열기OToolStripMenuItem.Click += new System.EventHandler(this.열기OToolStripMenuItem_Click);
            // 
            // 저장SToolStripMenuItem
            // 
            this.저장SToolStripMenuItem.Name = "저장SToolStripMenuItem";
            this.저장SToolStripMenuItem.Size = new System.Drawing.Size(180, 22);
            this.저장SToolStripMenuItem.Text = "저장(&S)";
            this.저장SToolStripMenuItem.Click += new System.EventHandler(this.저장SToolStripMenuItem_Click);
            // 
            // 도움말ToolStripMenuItem
            // 
            this.도움말ToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.도움말보기HToolStripMenuItem,
            this.피드백보내기ToolStripMenuItem,
            this.toolStripSeparator1,
            this.프로그램정보IToolStripMenuItem});
            this.도움말ToolStripMenuItem.Name = "도움말ToolStripMenuItem";
            this.도움말ToolStripMenuItem.Size = new System.Drawing.Size(72, 20);
            this.도움말ToolStripMenuItem.Text = "도움말(&H)";
            this.도움말ToolStripMenuItem.Click += new System.EventHandler(this.도움말ToolStripMenuItem_Click);
            // 
            // 도움말보기HToolStripMenuItem
            // 
            this.도움말보기HToolStripMenuItem.Name = "도움말보기HToolStripMenuItem";
            this.도움말보기HToolStripMenuItem.Size = new System.Drawing.Size(180, 22);
            this.도움말보기HToolStripMenuItem.Text = "도움말 보기(&H)";
            this.도움말보기HToolStripMenuItem.Click += new System.EventHandler(this.도움말보기HToolStripMenuItem_Click);
            // 
            // 피드백보내기ToolStripMenuItem
            // 
            this.피드백보내기ToolStripMenuItem.Name = "피드백보내기ToolStripMenuItem";
            this.피드백보내기ToolStripMenuItem.Size = new System.Drawing.Size(180, 22);
            this.피드백보내기ToolStripMenuItem.Text = "피드백 보내기(&F)";
            this.피드백보내기ToolStripMenuItem.Click += new System.EventHandler(this.피드백보내기ToolStripMenuItem_Click);
            // 
            // toolStripSeparator1
            // 
            this.toolStripSeparator1.Name = "toolStripSeparator1";
            this.toolStripSeparator1.Size = new System.Drawing.Size(177, 6);
            // 
            // 프로그램정보IToolStripMenuItem
            // 
            this.프로그램정보IToolStripMenuItem.Name = "프로그램정보IToolStripMenuItem";
            this.프로그램정보IToolStripMenuItem.Size = new System.Drawing.Size(180, 22);
            this.프로그램정보IToolStripMenuItem.Text = "프로그램 정보(I)";
            this.프로그램정보IToolStripMenuItem.Click += new System.EventHandler(this.프로그램정보IToolStripMenuItem_Click);
            // 
            // splitContainer1
            // 
            this.splitContainer1.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainer1.Location = new System.Drawing.Point(0, 0);
            this.splitContainer1.Name = "splitContainer1";
            // 
            // splitContainer1.Panel1
            // 
            this.splitContainer1.Panel1.Controls.Add(this.button_reset);
            this.splitContainer1.Panel1.Controls.Add(this.button1);
            this.splitContainer1.Panel1.Controls.Add(this.monthCalendar1);
            this.splitContainer1.Panel1.Controls.Add(this.label_nowTime);
            this.splitContainer1.Panel1.Controls.Add(this.label6);
            this.splitContainer1.Panel1.Controls.Add(this.label5);
            this.splitContainer1.Panel1.Controls.Add(this.label_scheduleName);
            this.splitContainer1.Panel1.Controls.Add(this.comboBox_endTime);
            this.splitContainer1.Panel1.Controls.Add(this.comboBox_startTime);
            this.splitContainer1.Panel1.Controls.Add(this.textBox_scheduleContents);
            this.splitContainer1.Panel1.Controls.Add(this.textBox_scheduleName);
            this.splitContainer1.Panel1.Controls.Add(this.label2);
            this.splitContainer1.Panel1.Controls.Add(this.button_addSchedule);
            this.splitContainer1.Panel1.Controls.Add(this.label1);
            this.splitContainer1.Panel1.Paint += new System.Windows.Forms.PaintEventHandler(this.splitContainer1_Panel1_Paint);
            // 
            // splitContainer1.Panel2
            // 
            this.splitContainer1.Panel2.Controls.Add(this.button_scheduleModify);
            this.splitContainer1.Panel2.Controls.Add(this.button_deleteSchedule);
            this.splitContainer1.Panel2.Controls.Add(this.listView_schedule);
            this.splitContainer1.Panel2.Controls.Add(this.label3);
            this.splitContainer1.Size = new System.Drawing.Size(769, 440);
            this.splitContainer1.SplitterDistance = 256;
            this.splitContainer1.TabIndex = 11;
            // 
            // button_reset
            // 
            this.button_reset.Location = new System.Drawing.Point(90, 403);
            this.button_reset.Name = "button_reset";
            this.button_reset.Size = new System.Drawing.Size(70, 23);
            this.button_reset.TabIndex = 26;
            this.button_reset.Text = "초기화";
            this.button_reset.UseVisualStyleBackColor = true;
            this.button_reset.Click += new System.EventHandler(this.button_reset_Click);
            // 
            // button1
            // 
            this.button1.BackColor = System.Drawing.SystemColors.MenuHighlight;
            this.button1.ForeColor = System.Drawing.SystemColors.ButtonHighlight;
            this.button1.Location = new System.Drawing.Point(67, 190);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(49, 20);
            this.button1.TabIndex = 7;
            this.button1.Text = "오늘";
            this.button1.UseVisualStyleBackColor = false;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // monthCalendar1
            // 
            this.monthCalendar1.BackColor = System.Drawing.SystemColors.WindowFrame;
            this.monthCalendar1.Location = new System.Drawing.Point(16, 50);
            this.monthCalendar1.MaxSelectionCount = 1;
            this.monthCalendar1.Name = "monthCalendar1";
            this.monthCalendar1.TabIndex = 6;
            this.monthCalendar1.DateChanged += new System.Windows.Forms.DateRangeEventHandler(this.monthCalendar1_DateChanged);
            // 
            // label_nowTime
            // 
            this.label_nowTime.AutoSize = true;
            this.label_nowTime.Location = new System.Drawing.Point(17, 221);
            this.label_nowTime.Name = "label_nowTime";
            this.label_nowTime.Size = new System.Drawing.Size(53, 12);
            this.label_nowTime.TabIndex = 25;
            this.label_nowTime.Text = "현재시각";
            this.label_nowTime.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.label_nowTime.Click += new System.EventHandler(this.label7_Click);
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(17, 302);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(29, 12);
            this.label6.TabIndex = 24;
            this.label6.Text = "내용";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(17, 276);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(29, 12);
            this.label5.TabIndex = 23;
            this.label5.Text = "시간";
            // 
            // label_scheduleName
            // 
            this.label_scheduleName.AutoSize = true;
            this.label_scheduleName.Location = new System.Drawing.Point(17, 249);
            this.label_scheduleName.Name = "label_scheduleName";
            this.label_scheduleName.Size = new System.Drawing.Size(29, 12);
            this.label_scheduleName.TabIndex = 22;
            this.label_scheduleName.Text = "제목";
            this.label_scheduleName.Click += new System.EventHandler(this.label_scheduleName_Click);
            // 
            // comboBox_endTime
            // 
            this.comboBox_endTime.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.comboBox_endTime.FormattingEnabled = true;
            this.comboBox_endTime.ImeMode = System.Windows.Forms.ImeMode.On;
            this.comboBox_endTime.IntegralHeight = false;
            this.comboBox_endTime.Location = new System.Drawing.Point(156, 273);
            this.comboBox_endTime.Name = "comboBox_endTime";
            this.comboBox_endTime.Size = new System.Drawing.Size(80, 20);
            this.comboBox_endTime.TabIndex = 17;
            this.comboBox_endTime.SelectedIndexChanged += new System.EventHandler(this.comboBox_endTime_SelectedIndexChanged);
            // 
            // comboBox_startTime
            // 
            this.comboBox_startTime.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.comboBox_startTime.FormattingEnabled = true;
            this.comboBox_startTime.IntegralHeight = false;
            this.comboBox_startTime.Items.AddRange(new object[] {
            "00:00",
            "01:00",
            "02:00",
            "03:00",
            "04:00",
            "05:00",
            "06:00",
            "07:00",
            "08:00",
            "09:00",
            "10:00",
            "11:00",
            "12:00",
            "13:00",
            "14:00",
            "15:00",
            "16:00",
            "17:00",
            "18:00",
            "19:00",
            "20:00",
            "21:00",
            "22:00",
            "23:00"});
            this.comboBox_startTime.Location = new System.Drawing.Point(61, 273);
            this.comboBox_startTime.Name = "comboBox_startTime";
            this.comboBox_startTime.Size = new System.Drawing.Size(80, 20);
            this.comboBox_startTime.TabIndex = 16;
            this.comboBox_startTime.SelectedIndexChanged += new System.EventHandler(this.comboBox_SelectedIndexChanged);
            // 
            // textBox_scheduleContents
            // 
            this.textBox_scheduleContents.Location = new System.Drawing.Point(61, 299);
            this.textBox_scheduleContents.Multiline = true;
            this.textBox_scheduleContents.Name = "textBox_scheduleContents";
            this.textBox_scheduleContents.Size = new System.Drawing.Size(175, 98);
            this.textBox_scheduleContents.TabIndex = 21;
            // 
            // textBox_scheduleName
            // 
            this.textBox_scheduleName.Location = new System.Drawing.Point(61, 246);
            this.textBox_scheduleName.Name = "textBox_scheduleName";
            this.textBox_scheduleName.Size = new System.Drawing.Size(175, 21);
            this.textBox_scheduleName.TabIndex = 20;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(142, 276);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(14, 12);
            this.label2.TabIndex = 19;
            this.label2.Text = "~";
            // 
            // button_addSchedule
            // 
            this.button_addSchedule.Location = new System.Drawing.Point(166, 403);
            this.button_addSchedule.Name = "button_addSchedule";
            this.button_addSchedule.Size = new System.Drawing.Size(70, 23);
            this.button_addSchedule.TabIndex = 18;
            this.button_addSchedule.Text = "일정 추가";
            this.button_addSchedule.UseVisualStyleBackColor = true;
            this.button_addSchedule.Click += new System.EventHandler(this.button_addSchedule_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("굴림", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.label1.Location = new System.Drawing.Point(10, 22);
            this.label1.Name = "label1";
            this.label1.Padding = new System.Windows.Forms.Padding(10);
            this.label1.Size = new System.Drawing.Size(100, 36);
            this.label1.TabIndex = 8;
            this.label1.Text = "Calendar";
            // 
            // button_scheduleModify
            // 
            this.button_scheduleModify.Location = new System.Drawing.Point(306, 403);
            this.button_scheduleModify.Name = "button_scheduleModify";
            this.button_scheduleModify.Size = new System.Drawing.Size(75, 23);
            this.button_scheduleModify.TabIndex = 15;
            this.button_scheduleModify.Text = "일정 수정";
            this.button_scheduleModify.UseVisualStyleBackColor = true;
            this.button_scheduleModify.Click += new System.EventHandler(this.button_scheduleModify_Click);
            // 
            // button_deleteSchedule
            // 
            this.button_deleteSchedule.Location = new System.Drawing.Point(387, 403);
            this.button_deleteSchedule.Name = "button_deleteSchedule";
            this.button_deleteSchedule.Size = new System.Drawing.Size(75, 23);
            this.button_deleteSchedule.TabIndex = 14;
            this.button_deleteSchedule.Text = "일정 삭제";
            this.button_deleteSchedule.UseVisualStyleBackColor = true;
            this.button_deleteSchedule.Click += new System.EventHandler(this.button_deleteSchedule_Click);
            // 
            // listView_schedule
            // 
            this.listView_schedule.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.date,
            this.time,
            this.scheduleName,
            this.scheduleContents});
            this.listView_schedule.HideSelection = false;
            this.listView_schedule.Location = new System.Drawing.Point(14, 50);
            this.listView_schedule.Name = "listView_schedule";
            this.listView_schedule.Size = new System.Drawing.Size(448, 347);
            this.listView_schedule.TabIndex = 13;
            this.listView_schedule.UseCompatibleStateImageBehavior = false;
            this.listView_schedule.View = System.Windows.Forms.View.Details;
            this.listView_schedule.SelectedIndexChanged += new System.EventHandler(this.listView_schedule_SelectedIndexChanged);
            // 
            // date
            // 
            this.date.Text = "날짜";
            this.date.Width = 80;
            // 
            // time
            // 
            this.time.Text = "시간";
            this.time.Width = 100;
            // 
            // scheduleName
            // 
            this.scheduleName.Text = "일정명";
            this.scheduleName.Width = 100;
            // 
            // scheduleContents
            // 
            this.scheduleContents.Text = "일정내용";
            this.scheduleContents.Width = 150;
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Font = new System.Drawing.Font("굴림", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.label3.Location = new System.Drawing.Point(11, 22);
            this.label3.Name = "label3";
            this.label3.Padding = new System.Windows.Forms.Padding(10);
            this.label3.Size = new System.Drawing.Size(171, 36);
            this.label3.TabIndex = 12;
            this.label3.Text = "What to do today";
            // 
            // timer1
            // 
            this.timer1.Enabled = true;
            this.timer1.Interval = 1000;
            this.timer1.Tick += new System.EventHandler(this.timer1_Tick_1);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(769, 440);
            this.Controls.Add(this.menuStrip1);
            this.Controls.Add(this.splitContainer1);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MainMenuStrip = this.menuStrip1;
            this.Name = "Form1";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "일정관리";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.menuStrip1.ResumeLayout(false);
            this.menuStrip1.PerformLayout();
            this.splitContainer1.Panel1.ResumeLayout(false);
            this.splitContainer1.Panel1.PerformLayout();
            this.splitContainer1.Panel2.ResumeLayout(false);
            this.splitContainer1.Panel2.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).EndInit();
            this.splitContainer1.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.MenuStrip menuStrip1;
        private System.Windows.Forms.ToolStripMenuItem 파일FToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem 새로만들기ToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem 열기OToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem 저장SToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem 도움말ToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem 도움말보기HToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem 피드백보내기ToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator1;
        private System.Windows.Forms.ToolStripMenuItem 프로그램정보IToolStripMenuItem;
        private System.Windows.Forms.SplitContainer splitContainer1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.MonthCalendar monthCalendar1;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.ComboBox comboBox_startTime;
        private System.Windows.Forms.Button button_addSchedule;
        private System.Windows.Forms.ComboBox comboBox_endTime;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.TextBox textBox_scheduleContents;
        private System.Windows.Forms.TextBox textBox_scheduleName;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label_scheduleName;
        private System.Windows.Forms.Label label_nowTime;
        private System.Windows.Forms.Timer timer1;
        private System.Windows.Forms.Button button_reset;
        private System.Windows.Forms.ListView listView_schedule;
        private System.Windows.Forms.ColumnHeader date;
        private System.Windows.Forms.ColumnHeader time;
        private System.Windows.Forms.ColumnHeader scheduleName;
        private System.Windows.Forms.ColumnHeader scheduleContents;
        private System.Windows.Forms.Button button_scheduleModify;
        private System.Windows.Forms.Button button_deleteSchedule;
    }
}

