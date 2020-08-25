namespace HyeonhoApp
{
    partial class Form2
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Form2));
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
            this.timer1 = new System.Windows.Forms.Timer(this.components);
            this.SuspendLayout();
            // 
            // button_reset
            // 
            this.button_reset.Location = new System.Drawing.Point(92, 386);
            this.button_reset.Name = "button_reset";
            this.button_reset.Size = new System.Drawing.Size(70, 23);
            this.button_reset.TabIndex = 40;
            this.button_reset.Text = "초기화";
            this.button_reset.UseVisualStyleBackColor = true;
            // 
            // button1
            // 
            this.button1.BackColor = System.Drawing.SystemColors.MenuHighlight;
            this.button1.ForeColor = System.Drawing.SystemColors.ButtonHighlight;
            this.button1.Location = new System.Drawing.Point(69, 173);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(49, 20);
            this.button1.TabIndex = 28;
            this.button1.Text = "오늘";
            this.button1.UseVisualStyleBackColor = false;
            // 
            // monthCalendar1
            // 
            this.monthCalendar1.BackColor = System.Drawing.SystemColors.WindowFrame;
            this.monthCalendar1.Location = new System.Drawing.Point(18, 33);
            this.monthCalendar1.MaxSelectionCount = 1;
            this.monthCalendar1.Name = "monthCalendar1";
            this.monthCalendar1.TabIndex = 27;
            // 
            // label_nowTime
            // 
            this.label_nowTime.AutoSize = true;
            this.label_nowTime.Location = new System.Drawing.Point(19, 204);
            this.label_nowTime.Name = "label_nowTime";
            this.label_nowTime.Size = new System.Drawing.Size(53, 12);
            this.label_nowTime.TabIndex = 39;
            this.label_nowTime.Text = "현재시각";
            this.label_nowTime.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(19, 285);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(29, 12);
            this.label6.TabIndex = 38;
            this.label6.Text = "내용";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(19, 259);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(29, 12);
            this.label5.TabIndex = 37;
            this.label5.Text = "시간";
            // 
            // label_scheduleName
            // 
            this.label_scheduleName.AutoSize = true;
            this.label_scheduleName.Location = new System.Drawing.Point(19, 232);
            this.label_scheduleName.Name = "label_scheduleName";
            this.label_scheduleName.Size = new System.Drawing.Size(29, 12);
            this.label_scheduleName.TabIndex = 36;
            this.label_scheduleName.Text = "제목";
            // 
            // comboBox_endTime
            // 
            this.comboBox_endTime.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.comboBox_endTime.FormattingEnabled = true;
            this.comboBox_endTime.IntegralHeight = false;
            this.comboBox_endTime.Items.AddRange(new object[] {
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
            this.comboBox_endTime.Location = new System.Drawing.Point(158, 256);
            this.comboBox_endTime.Name = "comboBox_endTime";
            this.comboBox_endTime.Size = new System.Drawing.Size(80, 20);
            this.comboBox_endTime.TabIndex = 31;
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
            this.comboBox_startTime.Location = new System.Drawing.Point(63, 256);
            this.comboBox_startTime.Name = "comboBox_startTime";
            this.comboBox_startTime.Size = new System.Drawing.Size(80, 20);
            this.comboBox_startTime.TabIndex = 30;
            this.comboBox_startTime.SelectedIndexChanged += new System.EventHandler(this.comboBox_startTime_SelectedIndexChanged);
            // 
            // textBox_scheduleContents
            // 
            this.textBox_scheduleContents.Location = new System.Drawing.Point(63, 282);
            this.textBox_scheduleContents.Multiline = true;
            this.textBox_scheduleContents.Name = "textBox_scheduleContents";
            this.textBox_scheduleContents.Size = new System.Drawing.Size(175, 98);
            this.textBox_scheduleContents.TabIndex = 35;
            // 
            // textBox_scheduleName
            // 
            this.textBox_scheduleName.Location = new System.Drawing.Point(63, 229);
            this.textBox_scheduleName.Name = "textBox_scheduleName";
            this.textBox_scheduleName.Size = new System.Drawing.Size(175, 21);
            this.textBox_scheduleName.TabIndex = 34;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(144, 259);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(14, 12);
            this.label2.TabIndex = 33;
            this.label2.Text = "~";
            // 
            // button_addSchedule
            // 
            this.button_addSchedule.Location = new System.Drawing.Point(168, 386);
            this.button_addSchedule.Name = "button_addSchedule";
            this.button_addSchedule.Size = new System.Drawing.Size(70, 23);
            this.button_addSchedule.TabIndex = 32;
            this.button_addSchedule.Text = "일정 수정";
            this.button_addSchedule.UseVisualStyleBackColor = true;
            this.button_addSchedule.Click += new System.EventHandler(this.button_addSchedule_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("굴림", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(129)));
            this.label1.Location = new System.Drawing.Point(12, 5);
            this.label1.Name = "label1";
            this.label1.Padding = new System.Windows.Forms.Padding(10);
            this.label1.Size = new System.Drawing.Size(100, 36);
            this.label1.TabIndex = 29;
            this.label1.Text = "Calendar";
            // 
            // timer1
            // 
            this.timer1.Tick += new System.EventHandler(this.timer1_Tick);
            // 
            // Form2
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(248, 418);
            this.Controls.Add(this.button_reset);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.monthCalendar1);
            this.Controls.Add(this.label_nowTime);
            this.Controls.Add(this.label6);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.label_scheduleName);
            this.Controls.Add(this.comboBox_endTime);
            this.Controls.Add(this.comboBox_startTime);
            this.Controls.Add(this.textBox_scheduleContents);
            this.Controls.Add(this.textBox_scheduleName);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.button_addSchedule);
            this.Controls.Add(this.label1);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "Form2";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "일정 수정";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button button_reset;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.MonthCalendar monthCalendar1;
        private System.Windows.Forms.Label label_nowTime;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label_scheduleName;
        private System.Windows.Forms.ComboBox comboBox_endTime;
        private System.Windows.Forms.ComboBox comboBox_startTime;
        private System.Windows.Forms.TextBox textBox_scheduleContents;
        private System.Windows.Forms.TextBox textBox_scheduleName;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button button_addSchedule;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Timer timer1;
    }
}