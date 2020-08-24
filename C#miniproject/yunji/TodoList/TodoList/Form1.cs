using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO;

namespace TodoList
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Form2 newForm = new Form2();

            if(newForm.ShowDialog() == DialogResult.OK)
            {
                checkedListBox1.Items.Add(newForm.addThing);
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            for (int i = checkedListBox1.Items.Count - 1; i >= 0; i--)
            {
                if (checkedListBox1.GetItemChecked(i))
                {
                    checkedListBox1.Items.Remove(checkedListBox1.Items[i]);
                }
            }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            SaveFileDialog saveFileDialog = new SaveFileDialog();

            saveFileDialog.InitialDirectory = "C:"; //초기 dir
            saveFileDialog.Title = "저장하기";
            saveFileDialog.DefaultExt = "txt"; //기본 확장명
            saveFileDialog.Filter = "Txt files(*.txt)|*.txt";

            if(saveFileDialog.ShowDialog() == DialogResult.OK)
            {
                StreamWriter writer = new StreamWriter(saveFileDialog.OpenFile());
                writer.WriteLine(textBox1.Text + "월 " + textBox2.Text + "일 " + " TodoList");

                for (int i = 0 ;i<= checkedListBox1.Items.Count - 1; i++)
                {
                    writer.WriteLine(checkedListBox1.Items[i].ToString());
                }

                writer.Dispose();
                writer.Close();
            }

        }

        private void button4_Click(object sender, EventArgs e)
        {
            List<string> lines = new List<string>();
            string text;

            OpenFileDialog openFileDialog = new OpenFileDialog();

            openFileDialog.InitialDirectory = "C:";
            openFileDialog.Title = "불러오기";
            openFileDialog.DefaultExt = "txt";
            openFileDialog.Filter = "Txt files(*.txt)|*.txt";

            if(openFileDialog.ShowDialog() == DialogResult.OK)
            {
                textBox1.Text = "";
                textBox2.Text = "";

                for (int i = checkedListBox1.Items.Count - 1; i >= 0; i--)
                {
                    checkedListBox1.Items.Remove(checkedListBox1.Items[i]);
                }

                StreamReader reader = new StreamReader(openFileDialog.OpenFile());
                while((text = reader.ReadLine()) != null)
                {
                    lines.Add(text);
                }

                for(int i = 0; i < lines.Count; i++)
                {
                    if(i == 0)
                    {
                        textBox1.Text = lines[i].Split(' ')[0].Split('월')[0];
                        textBox2.Text = lines[i].Split(' ')[1].Split('일')[0];
                    }
                    else
                    {
                        checkedListBox1.Items.Add(lines[i]);
                    }
                }

                reader.Dispose();
                reader.Close();

            }
        }

        private void checkBox1_CheckedChanged(object sender, EventArgs e)
        {
            
           for (int i = checkedListBox1.Items.Count - 1; i >= 0; i--)
            {
                checkedListBox1.SetItemChecked(i, checkBox1.Checked);

            }
        }

    }
}
